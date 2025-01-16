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
        task = new Task("Uborka", "Uborka v dome");
    }

    @Test
    void addTaskInHistory() {
        historyManager.addTaskInHistory(task);
        assertFalse(historyManager.getHistory().isEmpty());
    }

    @Test
    void saveOldDataInHistoryManager() {
        historyManager.addTaskInHistory(task);
        task = new Task(task.getId(),"Uborka2", "Uborka v dome2");
        Task task2 = historyManager.getHistory().getFirst();
        assertNotEquals(task.getName(), task2.getName(), "Не найдена нужная версия");
        assertNotEquals(task.getTaskDescription(), task2.getTaskDescription(), "Не найдена нужная версия");

    }

    @Test
    void maxElementsInHistory() {
        for (int i = 0; i <= 11 ; i++) {
            historyManager.addTaskInHistory(task);
        }
        assertEquals(10, historyManager.getHistory().size(), "Неверное количесвто элементов");
    }
}
