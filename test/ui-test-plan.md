# UI Test Plan

The cases below are executed by the project-specific `test-ui` skill. Output comparisons are exact.

## Test case: Add and manage all task types

Aim: Verify that todos, deadlines, and events are stored polymorphically and retain their type-specific details when listed or marked.

### Input

```input
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
mark 2
list
bye
```

### Expected output

```expected
____________________________________________________________
Phin
I'm Phin. Apparently I have to deal with this.
What do you want?
____________________________________________________________
    Got it. I've added this task:
      [T][ ] borrow book
    Now you have 1 tasks in the list.
____________________________________________________________
    Got it. I've added this task:
      [D][ ] return book (by: Sunday)
    Now you have 2 tasks in the list.
____________________________________________________________
    Got it. I've added this task:
      [E][ ] project meeting (from: Mon 2pm to: 4pm)
    Now you have 3 tasks in the list.
____________________________________________________________
    Nice! I've marked this task as done:
      [D][X] return book (by: Sunday)
____________________________________________________________
    Here are the tasks in your list:
    1.[T][ ] borrow book
    2.[D][X] return book (by: Sunday)
    3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
    Finally. Bye.
____________________________________________________________
```

## Test case: Preserve free-form deadline text

Aim: Verify that deadline dates and times are stored and displayed as strings without date parsing.

### Input

```input
deadline do homework /by no idea :-p
bye
```

### Expected output

```expected
____________________________________________________________
Phin
I'm Phin. Apparently I have to deal with this.
What do you want?
____________________________________________________________
    Got it. I've added this task:
      [D][ ] do homework (by: no idea :-p)
    Now you have 1 tasks in the list.
____________________________________________________________
    Finally. Bye.
____________________________________________________________
```
