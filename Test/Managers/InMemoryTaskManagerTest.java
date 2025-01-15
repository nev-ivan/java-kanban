package Managers;

import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    TaskManager taskManager = Managers.getDefault();

    @AfterEach
    void clearManagerAfterEach() {
        taskManager.deleteAllTasks();
    }

    @Test
    void addSubtaskInSubtask() {
        EpicTask epicTask = new EpicTask("Epic1", "newEpic");
        taskManager.addNewEpic(epicTask);
        SubTask subTask1 = new SubTask("subTask1", "testSubTask1", epicTask.getId());
        int subTask1Id = taskManager.addNewSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "testSubTask2", subTask1Id);
        int subTask2Id = taskManager.addNewSubTask(subTask2);
        assertTrue(subTask2Id < 0);
    }

    @Test
    void addDifferentTaskAndSearchForId() {
        EpicTask epicTask = new EpicTask("Epic1", "newEpic");
        taskManager.addNewEpic(epicTask);
        SubTask subTask1 = new SubTask("subTask1", "newSubTask", epicTask.getId());
        taskManager.addNewSubTask(subTask1);
        Task task1 = new Task("task1", "newTask");
        taskManager.addNewTask(task1);
        assertNotNull(taskManager.getEpicTask(epicTask.getId()));
        assertNotNull(taskManager.getSubTask(subTask1.getId()));
        assertNotNull(taskManager.getTask(task1.getId()));
    }

    @Test
    void managerDoNotChangeTask() {
        Task task1 = new Task("task1", "newTask");
        int task1Id = taskManager.addNewTask(task1);
        Task task2 = taskManager.getTask(task1Id);
        assertEquals(task1.getName(), task2.getName());
        assertEquals(task1.getTaskDescription(), task2.getTaskDescription());
    }

    @Test
    void deleteTaskFromMap() {
        Task task = new Task("name1", "Description");
        taskManager.addNewTask(task);
        assertNotNull(taskManager.getTask(task.getId()));
        taskManager.deleteTask(task.getId());
        assertNull(taskManager.getTask(task.getId()));
    }

    @Test
    void deleteSubTaskFromMap() {
        EpicTask epic = new EpicTask("epic", "epic");
        taskManager.addNewEpic(epic);
        SubTask subTask = new SubTask("name1", "Description", epic.getId());
        taskManager.addNewSubTask(subTask);
        assertNotNull(taskManager.getSubTask(subTask.getId()));
        taskManager.deleteSubTask(subTask.getId());
        assertNull(taskManager.getSubTask(subTask.getId()));
    }

    @Test
    void deleteEpicFromMap() {
        EpicTask epic = new EpicTask("epic", "epic");
        taskManager.addNewEpic(epic);
        assertNotNull(taskManager.getEpicTask(epic.getId()));
        taskManager.deleteEpic(epic.getId());
        assertNull(taskManager.getEpicTask(epic.getId()));
    }

    @Test
    void updateTaskTest() {
        Task task = new Task(1,"name", "Description");
        taskManager.addNewTask(task);
        task.setId(5);
        task.setName("name2");
        task.setTaskDescription("newDescription");
        task.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task);
        assertEquals(5, taskManager.getTask(task.getId()).getId());
        assertEquals("name2", taskManager.getTask(task.getId()).getName());
        assertEquals("newDescription", taskManager.getTask(task.getId()).getTaskDescription());
        assertEquals(TaskStatus.DONE, taskManager.getTask(task.getId()).getStatus());
    }

    @Test
    void updateSubTaskTest() {
        EpicTask epic = new EpicTask("epic", "epic");
        taskManager.addNewEpic(epic);
        SubTask subTask = new SubTask(1,"name1", "Description", epic.getId());
        taskManager.addNewSubTask(subTask);
        subTask.setId(5);
        taskManager.updateSubTask(subTask);
        assertEquals(5, taskManager.getSubTask(subTask.getId()).getId());
    }

    @Test
    void updateEpicTest() {
        EpicTask epic = new EpicTask(1,"epic", "epic");
        taskManager.addNewEpic(epic);
        epic.setId(5);
        taskManager.updateEpic(epic);
        assertEquals(5, taskManager.getEpicTask(epic.getId()).getId());
    }

    @Test
    void deleteAllSubTasksTest() {
        EpicTask epic = new EpicTask("epic", "epic");
        taskManager.addNewEpic(epic);
        SubTask subTask = new SubTask("subtask", "subtask", epic.getId());
        taskManager.addNewSubTask(subTask);
        SubTask subTask2 = new SubTask("subtask2", "subtask2", epic.getId());
        taskManager.addNewSubTask(subTask2);
        SubTask subTask3 = new SubTask("subtask3", "subtask3", epic.getId());
        taskManager.addNewSubTask(subTask3);
        assertFalse(taskManager.getSubTasks().isEmpty());
        assertFalse(epic.getSubTasksIds().isEmpty());
        taskManager.deleteAllSubTasks();
        assertTrue(taskManager.getSubTasks().isEmpty());
        assertTrue(epic.getSubTasksIds().isEmpty());
    }

    @Test
    void deleteAllEpicTest() {
        EpicTask epic = new EpicTask("epic", "epic");
        taskManager.addNewEpic(epic);
        EpicTask epic2 = new EpicTask("epic2", "epic2");
        taskManager.addNewEpic(epic2);
        SubTask subTask = new SubTask("subtask", "subtask", epic.getId());
        taskManager.addNewSubTask(subTask);
        SubTask subTask2 = new SubTask("subtask2", "subtask2", epic2.getId());
        taskManager.addNewSubTask(subTask2);
        SubTask subTask3 = new SubTask("subtask3", "subtask3", epic2.getId());
        taskManager.addNewSubTask(subTask3);
        assertFalse(taskManager.getSubTasks().isEmpty());
        assertFalse(taskManager.getEpicTasks().isEmpty());
        taskManager.deleteAllEpic();
        assertTrue(taskManager.getSubTasks().isEmpty());
        assertTrue(taskManager.getEpicTasks().isEmpty());
    }
}
