package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    File file;

    @BeforeEach
    void beforeEach() throws IOException {
        file = File.createTempFile("task", ".csv");
        taskManager = new FileBackedTaskManager(file);
        init();
    }

    @Test
    void saveAndLoadFromFileTest() {
        FileBackedTaskManager taskManagerTest = FileBackedTaskManager.loadFromFile(file);
        assertEquals(taskManager.getTasks(), taskManagerTest.getTasks(), "Найдены различия в Task");
        assertEquals(taskManager.getSubTasks(), taskManagerTest.getSubTasks(), "Найдены различия в SubTask");
        assertEquals(taskManager.getEpicTasks(), taskManagerTest.getEpicTasks(), "Найдены различия в EpicTask");
        assertEquals(taskManager.getHistory(), taskManagerTest.getHistory(), "История просмотров отличается");
    }

    @Test
    void deleteFromFileTest() {
        assertEquals(1, taskManager.taskMap.size(), "Некорректное значение");
        taskManager.deleteTask(task.getId());
        FileBackedTaskManager taskManagerTest = FileBackedTaskManager.loadFromFile(file);
        assertEquals(0, taskManagerTest.taskMap.size(), "Некорректное значение");
    }

    @Test
    void notChangeFromFileTest() {
        FileBackedTaskManager taskManagerTest = FileBackedTaskManager.loadFromFile(file);
        Task oldTask = taskManager.getTask(task.getId());
        Task newTask = taskManagerTest.getTask(task.getId());
        assertEquals(oldTask.getName(), newTask.getName(), "При чтении из файла изменилось имя");
        assertEquals(oldTask.getDescription(), newTask.getDescription(), "Изменилось описание");
        assertEquals(oldTask.getStatus(), newTask.getStatus(), "При чтении из файла изменился статус");
    }

    @Test
    void getHistoryFromFileTest() {
        taskManager.getTask(task.getId());
        taskManager.getSubTask(subTask.getId());
        taskManager.getEpicTask(epicTask.getId());
        FileBackedTaskManager taskManagerTest = FileBackedTaskManager.loadFromFile(file);
        assertEquals(3, taskManagerTest.getHistory().size(), "Некорректное значение");
    }

    @Test
    void loadFromEmptyFile() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubTasks();
        taskManager.deleteAllEpic();
        FileBackedTaskManager taskManagerTest = FileBackedTaskManager.loadFromFile(file);
        assertTrue(taskManagerTest.taskMap.isEmpty());
        assertTrue(taskManagerTest.subTaskMap.isEmpty());
        assertTrue(taskManagerTest.epicMap.isEmpty());
    }

    @Test
    void timeSortTasksLoadFromFileTest() {
        FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(file);
        assertEquals(2, taskManager2.getPrioritizedTasks().size(), "Отсортированный список не загружается из файла");
        assertEquals(taskManager.getPrioritizedTasks(), taskManager2.getPrioritizedTasks(),
                "Отсортированный список подгружается из файла некорректно");
    }
}
