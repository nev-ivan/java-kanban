package manager;

import task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    Task task1, task2, task3;

    @BeforeEach
    void beforeEach() {
        historyManager = Managers.getDefaultHistoryManager();
        task1 = new Task(1, "11", "111");
        task2 = new Task(2, "22", "222");
        task3 = new Task(3, "33", "333");
    }

    @Test
    void notEmptyBeforeAddTask() {
        historyManager.add(task1);
        assertFalse(historyManager.getHistory().isEmpty());
    }

    @Test
    void notDublicateInHistory() {
        historyManager.add(task1);
        historyManager.add(task1);
        assertEquals(1, historyManager.getHistory().size(), "Неверный результат");
    }

    @Test
    void deleteTaskFromBeginningHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        assertEquals(3, historyManager.getHistory().size(), "некорректное значение");
        historyManager.remove(task1.getId());
        assertEquals(List.of(task2, task3), historyManager.getHistory());
    }

    @Test
    void deleteTaskFromMiddleHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        assertEquals(3, historyManager.getHistory().size(), "некорректное значение");
        historyManager.remove(task2.getId());
        assertEquals(List.of(task1, task3), historyManager.getHistory());
    }

    @Test
    void deleteTaskFromEndHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        assertEquals(3, historyManager.getHistory().size(), "некорректное значение");
        historyManager.remove(task3.getId());
        assertEquals(List.of(task1, task2), historyManager.getHistory(), "некорректное значение");
    }
}
