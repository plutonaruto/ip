import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starts the Phin chatbot application.
 */
public class Phin {
    private static final String DIVIDER = "____________________________________________________________";

    /**
     * Runs Phin's command loop, storing tasks until the user enters {@code bye}.
     *
     * @param args command-line arguments; not used by this application
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        System.out.println(DIVIDER);
        System.out.println("Phin");
        System.out.println("I'm Phin. Apparently I have to deal with this.");
        System.out.println("What do you want?");
        System.out.println(DIVIDER);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.println("    Finally. Bye.");
                System.out.println(DIVIDER);
                break;
            }

            try {
                if (command.equals("list")) {
                    System.out.println("    Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println("    " + (i + 1) + "." + tasks.get(i));
                    }
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = parseTaskIndex(command, "mark", tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    System.out.println("    Fine. I've marked this task as done:");
                    System.out.println("      " + tasks.get(taskIndex));
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = parseTaskIndex(command, "unmark", tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    System.out.println("    Fine. I've marked this task as not done:");
                    System.out.println("      " + tasks.get(taskIndex));
                } else if (command.equals("todo") || command.startsWith("todo ")) {
                    String description = command.substring("todo".length()).trim();
                    requireText(description,
                            "A todo without a description? Give me something to work with.");
                    Task task = new Todo(description);
                    tasks.add(task);
                    printAddedTask(task, tasks.size());
                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    String detailsText = command.substring("deadline".length()).trim();
                    String[] details = detailsText.split("\\s+/by\\s+", 2);
                    if (details.length < 2 || details[0].isBlank() || details[1].isBlank()) {
                        throw new PhinException(
                                "Deadlines need a description and a time. Try: deadline TASK /by TIME");
                    }
                    Task task = new Deadline(details[0].trim(), details[1].trim());
                    tasks.add(task);
                    printAddedTask(task, tasks.size());
                } else if (command.equals("event") || command.startsWith("event ")) {
                    String detailsText = command.substring("event".length()).trim();
                    String[] descriptionAndTimes = detailsText.split("\\s+/from\\s+", 2);
                    String[] times = descriptionAndTimes.length < 2
                            ? new String[0]
                            : descriptionAndTimes[1].split("\\s+/to\\s+", 2);
                    if (descriptionAndTimes.length < 2 || times.length < 2
                            || descriptionAndTimes[0].isBlank()
                            || times[0].isBlank() || times[1].isBlank()) {
                        throw new PhinException(
                                "Events need all their details. Try: event TASK /from START /to END");
                    }
                    Task task = new Event(descriptionAndTimes[0].trim(),
                            times[0].trim(), times[1].trim());
                    tasks.add(task);
                    printAddedTask(task, tasks.size());
                } else {
                    throw new PhinException(
                            "That command means nothing to me. Try list, todo, deadline, event, mark, or unmark.");
                }
            } catch (PhinException exception) {
                System.out.println("    Seriously? " + exception.getMessage());
            }

            System.out.println(DIVIDER);
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
     * Converts a mark or unmark argument to a valid zero-based task index.
     *
     * @param command complete command entered by the user
     * @param commandWord command whose argument is being parsed
     * @param taskCount number of tasks currently stored
     * @return zero-based index of the selected task
     * @throws PhinException if the argument is missing, nonnumeric, or outside the list
     */
    private static int parseTaskIndex(String command, String commandWord, int taskCount)
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

    /**
     * Prints Phin's standard confirmation after adding a task.
     *
     * @param task task that was added
     * @param taskCount number of tasks now stored
     */
    private static void printAddedTask(Task task, int taskCount) {
        System.out.println("    Fine. I've added this task:");
        System.out.println("      " + task);
        System.out.println("    Now you have " + taskCount + " tasks in the list.");
    }
}
