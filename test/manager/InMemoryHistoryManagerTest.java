package manager;

import task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        int idOfTask2 = historyManager.getHistory().get(1).getId();
        int idOfTask3 = historyManager.getHistory().get(2).getId();
        historyManager.remove(task1.getId());
        assertEquals(2, historyManager.getHistory().size(), "некорректное значение");
        assertEquals(idOfTask2, historyManager.getHistory().get(0).getId());
        assertEquals(idOfTask3, historyManager.getHistory().get(1).getId());
    }

    @Test
    void deleteTaskFromMiddleHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        assertEquals(3, historyManager.getHistory().size(), "некорректное значение");
        int idOfTask3 = historyManager.getHistory().get(2).getId();
        int idOfTask1 = historyManager.getHistory().get(0).getId();
        historyManager.remove(task2.getId());
        assertEquals(2, historyManager.getHistory().size(), "некорректное значение");
        assertEquals(idOfTask3, historyManager.getHistory().get(1).getId());
        assertEquals(idOfTask1, historyManager.getHistory().get(0).getId());
    }

    @Test
    void deleteTaskFromEndHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        assertEquals(3, historyManager.getHistory().size(), "некорректное значение");
        int idOfTask1 = historyManager.getHistory().get(0).getId();
        int idOfTask2 = historyManager.getHistory().get(1).getId();
        historyManager.remove(task3.getId());
        assertEquals(2, historyManager.getHistory().size(), "некорректное значение");
        assertEquals(idOfTask1, historyManager.getHistory().get(0).getId(), "некорректное значение");
        assertEquals(idOfTask2, historyManager.getHistory().get(1).getId(), "некорректное значение");
    }
}
