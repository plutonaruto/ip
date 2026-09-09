package phin;

import java.io.IOException;
import java.util.List;

/**
 * Processes Phin commands independently of the console or graphical interface.
 */
public class Phin {
    private static final String LOADING_ERROR = "Couldn't load data/phin.txt. Check the file before restarting;"
            + " it has not been changed.";

    private final Storage storage = new Storage();
    private final TaskList tasks;

    /**
     * Loads saved tasks for a new chatbot session.
     */
    public Phin() {
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (IOException | SecurityException exception) {
            loadedTasks = null;
        }
        tasks = loadedTasks;
    }

    /**
     * Runs Phin through its original text interface.
     *
     * @param args Command-line arguments; not used by this application.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Phin phin = new Phin();

        ui.showWelcome();
        if (!phin.isReady()) {
            ui.showLoadingError();
            ui.showLine();
            return;
        }

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showResponse(phin.getResponse(command));
            ui.showLine();
            if (command.equals("bye")) {
                break;
            }
        }
    }

    /**
     * Executes one user command and returns the response for any user interface.
     *
     * @param command Complete, untrimmed user command.
     * @return Response text without a trailing line separator.
     */
    public String getResponse(String command) {
        if (!isReady()) {
            return "    " + LOADING_ERROR;
        }

        try {
            String commandWord = Parser.parseCommandWord(command);
            String response = execute(command, commandWord);
            if (!commandWord.equals("list") && !commandWord.equals("find") && !commandWord.equals("bye")) {
                try {
                    storage.save(tasks.asList());
                } catch (IOException | SecurityException exception) {
                    response += System.lineSeparator()
                            + "    Couldn't save data/phin.txt. Changes are only in memory; check the data folder.";
                }
            }
            return response;
        } catch (PhinException | IllegalArgumentException exception) {
            return "    Seriously? " + exception.getMessage();
        }
    }

    /**
     * Checks whether saved tasks were loaded safely.
     *
     * @return True when commands can be processed.
     */
    public boolean isReady() {
        return tasks != null;
    }

    private String execute(String command, String commandWord) throws PhinException {
        assert isReady() : "Commands must not execute after a failed load";
        switch (commandWord) {
            case "bye":
                return "    Finally. Bye.";
            case "list":
                return formatTasks("Here are the tasks in your list:", tasks.asList());
            case "find":
                String keyword = Parser.parseFindKeyword(command);
                return formatTasks("Here are the matching tasks in your list:", tasks.find(keyword));
            case "mark":
                // Fallthrough
            case "unmark":
                int taskIndex = Parser.parseTaskIndex(command, commandWord, tasks.size());
                boolean isDone = commandWord.equals("mark");
                Task updatedTask = tasks.setDone(taskIndex, isDone);
                return "    Fine. I've marked this task as " + (isDone ? "done" : "not done") + ":"
                        + System.lineSeparator() + "      " + updatedTask;
            case "delete":
                int deletedIndex = Parser.parseTaskIndex(command, commandWord, tasks.size());
                Task removedTask = tasks.delete(deletedIndex);
                return "    Noted. I've removed this task:" + System.lineSeparator() + "      " + removedTask
                        + System.lineSeparator() + formatTaskCount();
            default:
                Task task = Parser.parseTask(command);
                tasks.add(task);
                return "    Fine. I've added this task:" + System.lineSeparator() + "      " + task
                        + System.lineSeparator() + formatTaskCount();
        }
    }

    private String formatTasks(String heading, List<Task> displayedTasks) {
        StringBuilder response = new StringBuilder("    ").append(heading);
        for (int i = 0; i < displayedTasks.size(); i++) {
            response.append(System.lineSeparator()).append("    ").append(i + 1).append('.')
                    .append(displayedTasks.get(i));
        }
        return response.toString();
    }

    private String formatTaskCount() {
        return "    Now you have " + tasks.size() + " tasks in the list.";
    }
}
