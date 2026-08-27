package phin;

/** Interprets console commands without changing tasks or performing input/output. */
public class Parser {
    private Parser() {
        // Utility class: parsing does not need per-instance state.
    }

    /**
     * Recognizes command words using the existing exact-match and space rules.
     *
     * @param command complete, untrimmed input line
     * @return recognized command word
     * @throws PhinException if the command is unknown or takes unexpected arguments
     */
    public static String parseCommandWord(String command) throws PhinException {
        if (command.equals("list") || command.equals("bye")) {
            return command;
        }
        for (String word : new String[] {"mark", "unmark", "delete", "todo", "deadline", "event"}) {
            if (command.equals(word) || command.startsWith(word + " ")) {
                return word;
            }
        }
        throw new PhinException(
                "That command means nothing to me. Try list, todo, deadline, event, mark, unmark, or delete.");
    }

    /**
     * Constructs a task only after its command fields and dates are validated.
     *
     * @param command complete task-creation command
     * @return new task, not yet added to the list
     * @throws PhinException if the command is not a valid task-creation command
     * @throws IllegalArgumentException if a date or event range is invalid
     */
    public static Task parseTask(String command) throws PhinException {
        switch (parseCommandWord(command)) {
        case "todo":
            String description = command.substring("todo".length()).trim();
            requireText(description,
                    "A todo without a description? Give me something to work with.");
            return new Todo(description);
        case "deadline":
            String detailsText = command.substring("deadline".length()).trim();
            String[] details = detailsText.split("\\s+/by\\s+", 2);
            if (details.length < 2 || details[0].isBlank() || details[1].isBlank()) {
                throw new PhinException(
                        "Deadlines need a description and a time. Try: deadline TASK /by TIME");
            }
            return new Deadline(details[0].trim(), details[1].trim());
        case "event":
            String eventText = command.substring("event".length()).trim();
            String[] descriptionAndTimes = eventText.split("\\s+/from\\s+", 2);
            String[] times = descriptionAndTimes.length < 2
                    ? new String[0]
                    : descriptionAndTimes[1].split("\\s+/to\\s+", 2);
            if (descriptionAndTimes.length < 2 || times.length < 2
                    || descriptionAndTimes[0].isBlank()
                    || times[0].isBlank() || times[1].isBlank()) {
                throw new PhinException(
                        "Events need all their details. Try: event TASK /from START /to END");
            }
            return new Event(descriptionAndTimes[0].trim(),
                    times[0].trim(), times[1].trim());
        default:
            throw new PhinException("Expected a todo, deadline, or event command.");
        }
    }

    /**
     * Rejects a required command field when it contains no visible text.
     *
     * @param text field value to validate
     * @param errorMessage explanation shown when the field is blank
     * @throws PhinException if the field is blank
     */
    private static void requireText(String text, String errorMessage) throws PhinException {
        if (text.isBlank()) {
            throw new PhinException(errorMessage);
        }
    }

    /**
     * Converts a task-number argument to a valid zero-based task index.
     *
     * @param command complete command entered by the user
     * @param commandWord command whose argument is being parsed
     * @param taskCount number of tasks currently stored
     * @return zero-based index of the selected task
     * @throws PhinException if the argument is missing, nonnumeric, or outside the list
     */
    public static int parseTaskIndex(String command, String commandWord, int taskCount)
            throws PhinException {
        String indexText = command.substring(commandWord.length()).trim();
        if (indexText.isBlank()) {
            throw new PhinException("Tell me which task to " + commandWord
                    + ". Try: " + commandWord + " NUMBER");
        }

        final int taskNumber;
        try {
            taskNumber = Integer.parseInt(indexText);
        } catch (NumberFormatException exception) {
            throw new PhinException("Task numbers are, inconveniently, numbers. Try: "
                    + commandWord + " NUMBER");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new PhinException("Task " + taskNumber
                    + " isn't in the list. Pick a number from 1 to " + taskCount + ".");
        }
        return taskNumber - 1;
    }

}
