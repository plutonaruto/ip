# Phin

Phin is a chatbot written in Java. Given below are instructions on how to set it up.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Phin.java` file, right-click it, and choose `Run Phin.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see the following output:
   ```
   Hello! I'm Phin.
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Saved tasks

Run Phin with the project root as the working directory. Tasks are loaded from
`data/phin.txt` at startup and saved after add, mark, unmark, and delete commands.
The folder is created on the first save and is excluded from Git.

Records use `TYPE|STATUS|DESCRIPTION` with deadline or event time fields appended.
Types are T, D, and E; status is 0 or 1. Fields use UTF-8 URL encoding: spaces are
`+`, literal plus signs are `%2B`, and pipes are `%7C`.

If loading fails, Phin stops without changing the file. Correct or back up and
remove the damaged file before restarting. If saving fails, changes remain in
memory; fix the folder permissions and retry a change before exiting.

## Dates (Level 8)

Deadlines and event endpoints accept real calendar dates in `yyyy-MM-dd`
format, for example `deadline return book /by 2019-12-02` or
`event workshop /from 2019-12-02 /to 2019-12-03`. The task list displays
`Dec 02 2019`. Dates are stored as `LocalDate`; times of day are not supported.
An event may start and end on the same day, but cannot end before it starts.
Invalid dates are rejected without adding a task.

Saved dates remain in ISO format. Level 7 files containing free-form dates
such as `Sunday` must be backed up and edited to use `yyyy-MM-dd` before
startup. Phin refuses to load invalid dates and does not overwrite that file.
