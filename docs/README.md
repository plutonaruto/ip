# Phin User Guide

Phin is a task manager for todos, deadlines, and events.

## Getting help

Enter `help` (or its shortcut `h`) to see every command, its format, and its shortcut inside Phin.
Commands accept the following shortcuts: `l` for `list`, `t` for `todo`, `dl` for `deadline`, `e` for
`event`, `m` for `mark`, `um` for `unmark`, `del` for `delete`, `u` for `update`, `f` for `find`, and
`q` for `bye`.

## Updating a task

Use `update NUMBER` followed by one or more fields. Fields not included in the
command stay unchanged, as do the task type and completion status.

- Every task supports `/description TEXT`.
- Deadlines also support `/by yyyy-MM-dd`.
- Events also support `/from yyyy-MM-dd` and `/to yyyy-MM-dd`.

For example, this command changes only the end date of task 3:

`update 3 /to 2024-03-05`

Phin responds with the complete updated task:

```text
    Fine. I've updated this task:
      [E][ ] project meeting (from: Mar 01 2024 to: Mar 05 2024)
```

You may update multiple fields together:

`update 2 /description submit final report /by 2024-03-08`

Use each field at most once. Phin rejects blank, unknown, or type-incompatible
fields. It also rejects invalid dates and event ranges without changing the task.
