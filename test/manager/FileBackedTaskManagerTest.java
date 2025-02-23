package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.SubTask;
import task.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    Task task1;
    Task task2;
    Task task3;
    EpicTask task4;
    SubTask task5;
    FileBackedTaskManager taskManager;
    File file;

    @BeforeEach
    void beforeEach() throws IOException {
        task1 = new Task(1, "11", "111");
        task2 = new Task(2, "22", "222");
        task3 = new Task(3, "33", "333");
        task4 = new EpicTask(4, "44", "444");
        task5 = new SubTask(5, "55", "555", 4);
        file = File.createTempFile("task", ".csv");
        taskManager = FileBackedTaskManager.loadFromFile(file);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);
        taskManager.addNewEpic(task4);
        taskManager.addNewSubTask(task5);
    }

    @Test
    void saveAndLoadFromFileTest() {
        FileBackedTaskManager taskManagerTest = FileBackedTaskManager.loadFromFile(file);
        assertEquals(taskManager.taskMap, taskManagerTest.taskMap, "Функция работает некорректно.");
        assertEquals(3, taskManagerTest.taskMap.size(), "Некорректное значение");
        assertEquals(1, taskManagerTest.subTaskMap.size(), "Некорректное значение");
        assertEquals(1, taskManagerTest.epicMap.size(), "Некорректное значение");
    }

    @Test
    void deleteFromFileTest() {
        assertEquals(3, taskManager.taskMap.size(), "Некорректное значение");
        taskManager.deleteTask(task1.getId());
        FileBackedTaskManager taskManagerTest = FileBackedTaskManager.loadFromFile(file);
        assertEquals(2, taskManagerTest.taskMap.size(), "Некорректное значение");
    }

    @Test
    void notChangeFromFileTest() {
        FileBackedTaskManager taskManagerTest = FileBackedTaskManager.loadFromFile(file);
        Task oldTask = taskManager.getTask(task2.getId());
        Task newTask = taskManagerTest.getTask(task2.getId());
        assertEquals(oldTask.getName(), newTask.getName(), "При чтении из файла изменилось имя");
        assertEquals(oldTask.getTaskDescription(), newTask.getTaskDescription(), "Изменилось описание");
        assertEquals(oldTask.getStatus(), newTask.getStatus(), "При чтении из файла изменился статус");
    }

    @Test
    void getHistoryFromFileTest() {
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getEpicTask(task4.getId());
        taskManager.getSubTask(task5.getId());
        FileBackedTaskManager taskManagerTest = FileBackedTaskManager.loadFromFile(file);
        assertEquals(4, taskManagerTest.getHistory().size(), "Некорректное значение");
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
}
