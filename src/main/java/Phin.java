import java.util.Scanner;

/**
 * Starts the Phin chatbot application.
 */
public class Phin {
    private static final String DIVIDER = "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    /**
     * Runs Phin's command loop, storing tasks until the user enters {@code bye}.
     *
     * @param args command-line arguments; not used by this application
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

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

            if (command.equals("list")) {
                System.out.println("    Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("    " + (i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                String indexText = command.substring("mark ".length()).trim();

                try {
                    int taskIndex = Integer.parseInt(indexText) - 1;

                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        System.out.println("    That task number is not in your list.");
                    } else {
                        tasks[taskIndex].markAsDone();
                        System.out.println("    Nice! I've marked this task as done:");
                        System.out.println("      " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("    Please provide a valid task number after mark.");
                }
            } else if (command.startsWith("unmark ")) {
                String indexText = command.substring("unmark ".length()).trim();

                try {
                    int taskIndex = Integer.parseInt(indexText) - 1;

                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        System.out.println("    That task number is not in your list.");
                    } else {
                        tasks[taskIndex].markAsNotDone();
                        System.out.println("    OK, I've marked this task as not done yet:");
                        System.out.println("      " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("    Please provide a valid task number after unmark.");
                }
            } else if (command.startsWith("todo ")) {
                String description = command.substring("todo ".length());
                tasks[taskCount] = new Todo(description);
                taskCount++;
                System.out.println("    Got it. I've added this task:");
                System.out.println("      " + tasks[taskCount - 1]);
                System.out.println("    Now you have " + taskCount + " tasks in the list.");
            } else if (command.startsWith("deadline ")) {
                String[] details = command.substring("deadline ".length()).split(" /by ", 2);
                tasks[taskCount] = new Deadline(details[0], details[1]);
                taskCount++;
                System.out.println("    Got it. I've added this task:");
                System.out.println("      " + tasks[taskCount - 1]);
                System.out.println("    Now you have " + taskCount + " tasks in the list.");
            } else if (command.startsWith("event ")) {
                String[] descriptionAndTimes = command.substring("event ".length()).split(" /from ", 2);
                String[] times = descriptionAndTimes[1].split(" /to ", 2);
                tasks[taskCount] = new Event(descriptionAndTimes[0], times[0], times[1]);
                taskCount++;
                System.out.println("    Got it. I've added this task:");
                System.out.println("      " + tasks[taskCount - 1]);
                System.out.println("    Now you have " + taskCount + " tasks in the list.");
            }

            System.out.println(DIVIDER);
        }
    }
}
