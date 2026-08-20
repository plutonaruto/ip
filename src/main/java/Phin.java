import java.util.Scanner;

/**
 * Starts the Phin chatbot application.
 */
public class Phin {
    private static final String DIVIDER = "____________________________________________________________";

    /**
     * Runs Phin's command loop, echoing input until the user enters {@code bye}.
     *
     * @param args command-line arguments; not used by this application
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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

            System.out.println("    " + command + ". Yeah, I heard you the first time.");
            System.out.println(DIVIDER);
        }
    }
}
