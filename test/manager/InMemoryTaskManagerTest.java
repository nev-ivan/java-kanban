package manager;

import task.EpicTask;
import task.SubTask;
import task.Task;
import task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    TaskManager taskManager;
    EpicTask epicTask;
    SubTask subTask;
    Task task;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
        epicTask = new EpicTask("Epic", "epicDescription");
        taskManager.addNewEpic(epicTask);
        subTask = new SubTask(1, "subTask", "subTaskDescription", epicTask.getId());
        taskManager.addNewSubTask(subTask);
        task = new Task(1, "task", "taskDescription");
        taskManager.addNewTask(task);

    }

    @Test
    void addSubtaskInSubtask() {
        SubTask subTask2 = new SubTask("subTask2", "testSubTask2", subTask.getId());
        int subTask2Id = taskManager.addNewSubTask(subTask2);
        assertTrue(subTask2Id < 0);
    }

    @Test
    void addDifferentTaskAndSearchForId() {
        assertNotNull(taskManager.getEpicTask(epicTask.getId()));
        assertNotNull(taskManager.getSubTask(subTask.getId()));
        assertNotNull(taskManager.getTask(task.getId()));
    }

    @Test
    void managerDoNotChangeTask() {
        Task task2 = taskManager.getTask(task.getId());
        assertEquals(task.getName(), task2.getName());
        assertEquals(task.getTaskDescription(), task2.getTaskDescription());
    }

    @Test
    void deleteTaskFromMap() {
        assertNotNull(taskManager.getTask(task.getId()));
        taskManager.deleteTask(task.getId());
        assertNull(taskManager.getTask(task.getId()));
    }

    @Test
    void deleteSubTaskFromMap() {
        assertNotNull(taskManager.getSubTask(subTask.getId()));
        taskManager.deleteSubTask(subTask.getId());
        assertNull(taskManager.getSubTask(subTask.getId()));
    }

    @Test
    void deleteEpicFromMap() {
        assertNotNull(taskManager.getEpicTask(epicTask.getId()));
        taskManager.deleteEpic(epicTask.getId());
        assertNull(taskManager.getEpicTask(epicTask.getId()));
    }

    @Test
    void updateTaskTest() {
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
        subTask.setId(5);
        taskManager.updateSubTask(subTask);
        assertEquals(5, taskManager.getSubTask(subTask.getId()).getId());
    }

    @Test
    void updateEpicTest() {
        epicTask.setId(5);
        taskManager.updateEpic(epicTask);
        assertEquals(5, taskManager.getEpicTask(epicTask.getId()).getId());
    }

    @Test
    void deleteAllSubTasksTest() {
        assertFalse(taskManager.getSubTasks().isEmpty());
        assertFalse(epicTask.getSubTasksIds().isEmpty());
        taskManager.deleteAllSubTasks();
        assertTrue(taskManager.getSubTasks().isEmpty());
        assertTrue(epicTask.getSubTasksIds().isEmpty());
    }

    @Test
    void deleteAllEpicTest() {
        assertFalse(taskManager.getSubTasks().isEmpty());
        assertFalse(taskManager.getEpicTasks().isEmpty());
        taskManager.deleteAllEpic();
        assertTrue(taskManager.getSubTasks().isEmpty());
        assertTrue(taskManager.getEpicTasks().isEmpty());
    }
}
