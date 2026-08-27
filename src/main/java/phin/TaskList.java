package phin;

import java.util.ArrayList;
import java.util.List;

/** Owns the ordered task collection and provides operations that change it. */
public class TaskList {
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this(List.of());
    }

    /**
     * Copies the loaded collection so callers cannot add or remove tasks through it.
     * Task objects themselves are shared, preserving their saved completion status.
     *
     * @param loadedTasks tasks loaded from storage, in their saved order
     */
    public TaskList(List<Task> loadedTasks) {
        tasks = new ArrayList<>(loadedTasks);
    }

    public int size() {
        return tasks.size();
    }

    public void add(Task task) {
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
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.description.contains(keyword)) {
                matches.add(task);
            }
        }
        return List.copyOf(matches);
    }

    /** Removes the task at a validated zero-based index and returns it for display. */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Updates completion status and returns the affected task for display.
     *
     * @param index zero-based index already validated by the parser
     * @param isDone whether the task should be marked done
     * @return the updated task
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
     * Returns an unmodifiable snapshot of the ordering for display and storage.
     * The task objects are shared; this prevents collection edits, not task edits.
     */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
