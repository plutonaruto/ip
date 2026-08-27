package phin;

import java.io.IOException;

/**
 * Starts the Phin chatbot application.
 */
public class Phin {
    /**
     * Creates an application entry-point instance.
     */
    public Phin() {
    }

    /**
     * Loads saved tasks and runs the command loop, saving after each accepted change.
     *
     * @param args command-line arguments; not used by this application.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage();
        TaskList tasks;

        ui.showWelcome();

        try {
            tasks = new TaskList(storage.load());
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
                        ui.showTasks(tasks.asList());
                        break;
                    case "mark":
                        // Fallthrough
                    case "unmark":
                        int taskIndex = Parser.parseTaskIndex(command, commandWord, tasks.size());
                        boolean isDone = commandWord.equals("mark");
                        Task updatedTask = tasks.setDone(taskIndex, isDone);
                        ui.showMarkedTask(updatedTask, isDone);
                        break;
                    case "delete":
                        int deletedIndex = Parser.parseTaskIndex(command, commandWord, tasks.size());
                        Task removedTask = tasks.delete(deletedIndex);
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
                        storage.save(tasks.asList());
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
