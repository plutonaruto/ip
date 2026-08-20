# UI Test Plan

The cases below are executed by the project-specific `test-ui` skill. Output comparisons are exact.

## Test case: Existing Level 3 workflow

Aim: Verify that adding, listing, marking, and unmarking a task still work before Level 4 is implemented.

### Input

```input
read book
list
mark 1
unmark 1
bye
```

### Expected output

```expected
____________________________________________________________
Phin
I'm Phin. Apparently I have to deal with this.
What do you want?
____________________________________________________________
    added: read book
____________________________________________________________
    Here are the tasks in your list:
    1.[ ] read book
____________________________________________________________
    Nice! I've marked this task as done:
      [X] read book
____________________________________________________________
    OK, I've marked this task as not done yet:
      [ ] read book
____________________________________________________________
    Finally. Bye.
____________________________________________________________
```
