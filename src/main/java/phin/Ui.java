package phin;

import java.util.List;
import java.util.Scanner;

/** Reads console commands and formats all user-facing responses for Phin. */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private final Scanner scanner = new Scanner(System.in);

    /** Returns false at end of input so the application can exit without an error. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Reads the next command unchanged, after checking hasNextCommand(). */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Prints the greeting between divider lines. */
    public void showWelcome() {
        showLine();
        System.out.println("Phin");
        System.out.println("I'm Phin. Apparently I have to deal with this.");
        System.out.println("What do you want?");
        showLine();
    }

    public void showLine() {
        System.out.println(DIVIDER);
    }

    public void showGoodbye() {
        System.out.println("    Finally. Bye.");
    }

    /** Displays tasks with the one-based numbers used in commands. */
    public void showTasks(List<Task> tasks) {
        System.out.println("    Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("    " + (i + 1) + "." + tasks.get(i));
        }
    }

    /** Confirms a status change without modifying the task. */
    public void showMarkedTask(Task task, boolean isDone) {
        System.out.println("    Fine. I've marked this task as " + (isDone ? "done" : "not done") + ":");
        System.out.println("      " + task);
    }

    /** Confirms addition and reports the resulting list size. */
    public void showAddedTask(Task task, int taskCount) {
        System.out.println("    Fine. I've added this task:");
        System.out.println("      " + task);
        showTaskCount(taskCount);
    }

    /** Confirms deletion and reports the resulting list size. */
    public void showDeletedTask(Task task, int taskCount) {
        System.out.println("    Noted. I've removed this task:");
        System.out.println("      " + task);
        showTaskCount(taskCount);
    }

    private void showTaskCount(int taskCount) {
        System.out.println("    Now you have " + taskCount + " tasks in the list.");
    }

    public void showError(String message) {
        System.out.println("    Seriously? " + message);
    }

    public void showLoadingError() {
        System.out.println("    Couldn't load data/phin.txt. Check the file before restarting; it has not been changed.");
    }

    public void showSavingError() {
        System.out.println("    Couldn't save data/phin.txt. Changes are only in memory; check the data folder.");
    }
}
