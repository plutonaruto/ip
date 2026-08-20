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
    Fine. I've added this task:
      [T][ ] borrow book
    Now you have 1 tasks in the list.
____________________________________________________________
    Fine. I've added this task:
      [D][ ] return book (by: Sunday)
    Now you have 2 tasks in the list.
____________________________________________________________
    Fine. I've added this task:
      [E][ ] project meeting (from: Mon 2pm to: 4pm)
    Now you have 3 tasks in the list.
____________________________________________________________
    Fine. I've marked this task as done:
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

## Test case: Reject invalid mark and unmark commands

Aim: Verify that missing, nonnumeric, and out-of-range task numbers produce specific guidance without changing task state.

### Input

```input
todo test Phin
mark
mark two
mark 2
mark 1
unmark 0
unmark 1
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
    Fine. I've added this task:
      [T][ ] test Phin
    Now you have 1 tasks in the list.
____________________________________________________________
    Seriously? Tell me which task to mark. Try: mark NUMBER
____________________________________________________________
    Seriously? Task numbers are, inconveniently, numbers. Try: mark NUMBER
____________________________________________________________
    Seriously? Task 2 isn't in the list. Pick a number from 1 to 1.
____________________________________________________________
    Fine. I've marked this task as done:
      [T][X] test Phin
____________________________________________________________
    Seriously? Task 0 isn't in the list. Pick a number from 1 to 1.
____________________________________________________________
    Fine. I've marked this task as not done:
      [T][ ] test Phin
____________________________________________________________
    Here are the tasks in your list:
    1.[T][ ] test Phin
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
    Fine. I've added this task:
      [D][ ] do homework (by: no idea :-p)
    Now you have 1 tasks in the list.
____________________________________________________________
    Finally. Bye.
____________________________________________________________
```

## Test case: Reject malformed task commands without changing the list

Aim: Verify that missing descriptions and scheduling details produce specific guidance, while valid commands before and after each error still work.

### Input

```input
todo read book
todo
deadline return book
deadline submit report /by Friday
event meeting /from 2pm
event demo /from 3pm /to 4pm
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
    Fine. I've added this task:
      [T][ ] read book
    Now you have 1 tasks in the list.
____________________________________________________________
    Seriously? A todo without a description? Give me something to work with.
____________________________________________________________
    Seriously? Deadlines need a description and a time. Try: deadline TASK /by TIME
____________________________________________________________
    Fine. I've added this task:
      [D][ ] submit report (by: Friday)
    Now you have 2 tasks in the list.
____________________________________________________________
    Seriously? Events need all their details. Try: event TASK /from START /to END
____________________________________________________________
    Fine. I've added this task:
      [E][ ] demo (from: 3pm to: 4pm)
    Now you have 3 tasks in the list.
____________________________________________________________
    Here are the tasks in your list:
    1.[T][ ] read book
    2.[D][ ] submit report (by: Friday)
    3.[E][ ] demo (from: 3pm to: 4pm)
____________________________________________________________
    Finally. Bye.
____________________________________________________________
```

## Test case: Reject an unknown command

Aim: Verify that an unrecognised command produces guidance instead of being silently ignored.

### Input

```input
blah
bye
```

### Expected output

```expected
____________________________________________________________
Phin
I'm Phin. Apparently I have to deal with this.
What do you want?
____________________________________________________________
    Seriously? That command means nothing to me. Try list, todo, deadline, event, mark, unmark, or delete.
____________________________________________________________
    Finally. Bye.
____________________________________________________________
```

## Test case: Delete a task and renumber the remaining list

Aim: Verify that deleting a task reports the removed task and that later tasks move up to close the gap.

### Input

```input
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo join sports club
delete 3
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
    Fine. I've added this task:
      [T][ ] read book
    Now you have 1 tasks in the list.
____________________________________________________________
    Fine. I've added this task:
      [D][ ] return book (by: June 6th)
    Now you have 2 tasks in the list.
____________________________________________________________
    Fine. I've added this task:
      [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
    Now you have 3 tasks in the list.
____________________________________________________________
    Fine. I've added this task:
      [T][ ] join sports club
    Now you have 4 tasks in the list.
____________________________________________________________
    Noted. I've removed this task:
      [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
    Now you have 3 tasks in the list.
____________________________________________________________
    Here are the tasks in your list:
    1.[T][ ] read book
    2.[D][ ] return book (by: June 6th)
    3.[T][ ] join sports club
____________________________________________________________
    Finally. Bye.
____________________________________________________________
```

## Test case: Reject invalid delete commands

Aim: Verify that missing, nonnumeric, and out-of-range task numbers do not remove tasks, while a later valid deletion still works.

### Input

```input
todo keep this
delete
todo remove this
delete two
delete 3
delete 2
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
    Fine. I've added this task:
      [T][ ] keep this
    Now you have 1 tasks in the list.
____________________________________________________________
    Seriously? Tell me which task to delete. Try: delete NUMBER
____________________________________________________________
    Fine. I've added this task:
      [T][ ] remove this
    Now you have 2 tasks in the list.
____________________________________________________________
    Seriously? Task numbers are, inconveniently, numbers. Try: delete NUMBER
____________________________________________________________
    Seriously? Task 3 isn't in the list. Pick a number from 1 to 2.
____________________________________________________________
    Noted. I've removed this task:
      [T][ ] remove this
    Now you have 1 tasks in the list.
____________________________________________________________
    Here are the tasks in your list:
    1.[T][ ] keep this
____________________________________________________________
    Finally. Bye.
____________________________________________________________
```
