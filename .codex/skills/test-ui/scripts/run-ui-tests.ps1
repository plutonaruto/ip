param(
    [string]$TestPlan = "test/ui-test-plan.md"
)

$ErrorActionPreference = "Stop"

$javaVersion = & javac -version
if ($javaVersion -notmatch '^javac 25\.') {
    throw "Java 25 is required, but the active compiler is: $javaVersion"
}

if (-not (Test-Path -LiteralPath $TestPlan)) {
    throw "UI test plan not found: $TestPlan"
}

$plan = Get-Content -Raw -LiteralPath $TestPlan
$casePattern = '(?ms)^## Test case: (?<name>.+?)\r?\n\r?\nAim: (?<aim>.+?)\r?\n\r?\n### Input\r?\n\r?\n```input\r?\n(?<input>.*?)\r?\n```\r?\n\r?\n### Expected output\r?\n\r?\n```expected\r?\n(?<expected>.*?)\r?\n```'
$cases = [regex]::Matches($plan, $casePattern)
if ($cases.Count -eq 0) {
    throw "No test cases found in $TestPlan"
}

$buildDirectory = Join-Path ([System.IO.Path]::GetTempPath()) ("phin-ui-tests-" + [guid]::NewGuid())
New-Item -ItemType Directory -Path $buildDirectory | Out-Null

try {
    $sourceFiles = Get-ChildItem -LiteralPath "src/main/java" -Filter "*.java" | ForEach-Object FullName
    & javac -d $buildDirectory $sourceFiles
    if ($LASTEXITCODE -ne 0) {
        throw "Compilation failed."
    }

    foreach ($testCase in $cases) {
        $name = $testCase.Groups['name'].Value.Trim()
        $aim = $testCase.Groups['aim'].Value.Trim()
        $inputText = $testCase.Groups['input'].Value -replace "`r`n", "`n"
        $expected = $testCase.Groups['expected'].Value -replace "`r`n", "`n"

        $actualLines = $inputText | & java -cp $buildDirectory Phin
        $actual = ($actualLines -join "`n")

        Write-Output "=== $name ==="
        Write-Output "Aim: $aim"
        Write-Output "Console input:"
        Write-Output $inputText
        Write-Output "Console output:"
        Write-Output $actual

        if ($actual -cne $expected) {
            Write-Output "Expected output:"
            Write-Output $expected
            throw "UI test failed: $name"
        }

        Write-Output "Result: PASS"
    }

    Write-Output "All $($cases.Count) UI test case(s) passed."
} finally {
    if (Test-Path -LiteralPath $buildDirectory) {
        Remove-Item -LiteralPath $buildDirectory -Recurse -Force
    }
}
