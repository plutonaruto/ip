package phin;

import java.util.ArrayList;
import java.util.List;

/**
 * Owns the ordered task collection and provides operations that change it.
 */
public class TaskList {
    /**
     * Ordered collection owned by this task list.
     */
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this(List.of());
    }

    /**
     * Copies the loaded collection so callers cannot add or remove tasks through it.
     * Task objects themselves are shared, preserving their saved completion status.
     *
     * @param loadedTasks tasks loaded from storage, in their saved order.
     */
    public TaskList(List<Task> loadedTasks) {
        tasks = new ArrayList<>(loadedTasks);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The current task count.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Appends a task to the end of the list.
     *
     * @param task Task to append.
     */
    public void add(Task task) {
        assert task != null : "Only constructed tasks may be added";
        tasks.add(task);
    }

    /**
     * Finds tasks whose descriptions contain the case-sensitive search phrase.
     * The result preserves order and shares tasks, but cannot change collection membership.
     *
     * @param keyword Nonblank search phrase validated by the parser.
     * @return Unmodifiable matching tasks in their original order.
     */
    public List<Task> find(String keyword) {
        assert keyword != null && !keyword.isBlank() : "The parser must validate the search phrase";
        return tasks.stream()
                .filter(task -> task.description.contains(keyword))
                .toList();
    }

    /**
     * Removes and returns the task at a zero-based index.
     *
     * @param index Zero-based index of the task to remove.
     * @return The removed task.
     * @throws IndexOutOfBoundsException If the index is outside the list.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Updates completion status and returns the affected task for display.
     *
     * @param index Zero-based index already validated by the parser.
     * @param isDone Whether the task should be marked done.
     * @return The updated task.
     * @throws IndexOutOfBoundsException If the index is outside the list.
     */
    public Task setDone(int index, boolean isDone) {
        Task task = tasks.get(index);
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        return task;
    }

    /**
     * Returns an unmodifiable snapshot of the current task ordering.
     * Task objects are shared; collection edits are prevented, but task edits remain visible.
     *
     * @return An unmodifiable list containing the same task objects.
     * @throws NullPointerException If the list contains a null task.
     */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
