package phin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/** Checks task ordering, status updates, and collection ownership. */
class TaskListTest {
    @Test
    void constructor_loadedList_copiesCollectionButPreservesTasks() {
        Task task = new Todo("saved");
        task.markAsDone();
        List<Task> loaded = new ArrayList<>(List.of(task));
        TaskList tasks = new TaskList(loaded);
        loaded.clear();
        assertEquals(1, tasks.size());
        assertSame(task, tasks.asList().get(0));
        assertTrue(tasks.asList().get(0).isDone);
    }

    @Test
    void delete_middleAndRemainingTasks_preservesOrderAndBecomesEmpty() {
        Task first = new Todo("first");
        Task middle = new Deadline("middle", "2024-03-01");
        Task last = new Event("last", "2024-03-01", "2024-03-02");
        TaskList tasks = new TaskList();
        tasks.add(first);
        tasks.add(middle);
        tasks.add(last);
        assertSame(middle, tasks.delete(1));
        assertEquals(List.of(first, last), tasks.asList());
        assertSame(first, tasks.delete(0));
        assertSame(last, tasks.delete(0));
        assertEquals(0, tasks.size());
    }

    @Test
    void setDone_markAndUnmark_changesOnlySelectedTask() {
        Task first = new Todo("first");
        Task second = new Todo("second");
        TaskList tasks = new TaskList(List.of(first, second));
        assertSame(second, tasks.setDone(1, true));
        tasks.setDone(1, true);
        assertTrue(second.isDone);
        assertFalse(first.isDone);
        assertSame(second, tasks.setDone(1, false));
        tasks.setDone(1, false);
        assertFalse(second.isDone);
    }

    @Test
    void asList_snapshot_disallowsCollectionEditsAndKeepsOldOrdering() {
        Task first = new Todo("first");
        TaskList tasks = new TaskList(List.of(first));
        List<Task> snapshot = tasks.asList();
        assertThrows(UnsupportedOperationException.class, () -> snapshot.add(new Todo("external")));
        assertThrows(UnsupportedOperationException.class, () -> snapshot.remove(0));
        tasks.add(new Todo("second"));
        assertEquals(List.of(first), snapshot);
        assertEquals(2, tasks.size());
        tasks.setDone(0, true);
        assertTrue(snapshot.get(0).isDone); // Snapshots intentionally share task objects.
    }

    @Test
    void mutations_invalidIndices_leaveListUnchanged() {
        Task task = new Todo("keep");
        TaskList tasks = new TaskList(List.of(task));
        for (int index : new int[] {-1, 1}) {
            assertThrows(IndexOutOfBoundsException.class, () -> tasks.delete(index));
            assertThrows(IndexOutOfBoundsException.class, () -> tasks.setDone(index, true));
        }
        assertEquals(List.of(task), tasks.asList());
        assertFalse(task.isDone);
    }
}
