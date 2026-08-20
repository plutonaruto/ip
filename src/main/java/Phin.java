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
        String[] tasks = new String[MAX_TASKS];
        boolean[] completedTasks = new boolean[MAX_TASKS];
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
                    String status = completedTasks[i] ? "X" : " ";
                    System.out.println("    " + (i + 1) + ".[" + status + "] " + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                String indexText = command.substring("mark ".length()).trim();

                try {
                    int taskIndex = Integer.parseInt(indexText) - 1;

                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        System.out.println("    That task number is not in your list.");
                    } else {
                        completedTasks[taskIndex] = true;
                        System.out.println("    Nice! I've marked this task as done:");
                        System.out.println("      [X] " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("    Please provide a valid task number after mark.");
                }
            } else {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("    added: " + command);
            }

            System.out.println(DIVIDER);
        }
    }
}
