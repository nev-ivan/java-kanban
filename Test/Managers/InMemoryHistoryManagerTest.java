package Managers;

import Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();

    }

    @Test
    void addTaskInHistory() {
        Task task1 = new Task("Uborka", "Uborka v dome");
        int task1Id = taskManager.addNewTask(task1);
        task1 = taskManager.getTask(task1Id);
        assertTrue(!taskManager.getHistory().isEmpty());
    }

    @Test
    void saveOldDataInHistoryManager() {
        Task task1 = new Task("Uborka", "Uborka v dome");
        int task1Id = taskManager.addNewTask(task1);
        taskManager.getTask(task1Id);
        task1 = new Task(task1Id,"Uborka2", "Uborka v dome2");
        Task task2 = taskManager.getHistory().get(0);
        assertNotEquals(task1.getName(), task2.getName(), "Не найдена нужная версия");
        assertNotEquals(task1.getTaskDescription(), task2.getTaskDescription(), "Не найдена нужная версия");

    }

    @Test
    void maxElementsInHistory() {
        Task task1 = new Task("Uborka", "Uborka v dome");
        Task task2 = new Task("Uborka2", "Uborka v dome2");
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        assertEquals(10, taskManager.getHistory().size(), "Неверное количесвто элементов");
        taskManager.getTask(task1.getId());
        assertEquals(10, taskManager.getHistory().size(), "Неверное количесвто элементов");
    }
}
