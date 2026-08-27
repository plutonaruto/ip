import java.io.IOException;
import java.util.ArrayList;

/**
 * Starts the Phin chatbot application.
 */
public class Phin {
    /**
     * Loads saved tasks and runs the command loop, saving after each accepted change.
     *
     * @param args command-line arguments; not used by this application
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage();
        ArrayList<Task> tasks;

        ui.showWelcome();

        try {
            tasks = storage.load();
        } catch (IOException | SecurityException exception) {
            ui.showLoadingError();
            ui.showLine();
            return;
        }

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();

            try {
                String commandWord = Parser.parseCommandWord(command);
                if (commandWord.equals("bye")) {
                    ui.showGoodbye();
                    ui.showLine();
                    break;
                }
                switch (commandWord) {
                case "list":
                    ui.showTasks(tasks);
                    break;
                case "mark":
                case "unmark":
                    int taskIndex = Parser.parseTaskIndex(command, commandWord, tasks.size());
                    boolean isDone = commandWord.equals("mark");
                    if (isDone) {
                        tasks.get(taskIndex).markAsDone();
                    } else {
                        tasks.get(taskIndex).markAsNotDone();
                    }
                    ui.showMarkedTask(tasks.get(taskIndex), isDone);
                    break;
                case "delete":
                    int deletedIndex = Parser.parseTaskIndex(command, commandWord, tasks.size());
                    Task removedTask = tasks.remove(deletedIndex);
                    ui.showDeletedTask(removedTask, tasks.size());
                    break;
                default:
                    Task task = Parser.parseTask(command);
                    tasks.add(task);
                    ui.showAddedTask(task, tasks.size());
                    break;
                }
                if (!commandWord.equals("list")) {
                    try {
                        storage.save(tasks);
                    } catch (IOException | SecurityException exception) {
                        ui.showSavingError();
                    }
                }
            } catch (PhinException | IllegalArgumentException exception) {
                ui.showError(exception.getMessage());
            }

            ui.showLine();
        }
    }

}
