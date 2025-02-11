package manager;

import task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    Task task;

    @BeforeEach
    void beforeEach() {
        historyManager = Managers.getDefaultHistoryManager();
        task = new Task(1, "Uborka", "Uborka v dome");
    }

    @Test
    void notEmptyBeforeAddTask() {
        historyManager.add(task);
        assertFalse(historyManager.getHistory().isEmpty());
    }

    @Test
    void notDublicateInHistory() {
        historyManager.add(task);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Неверный результат");
    }
}
