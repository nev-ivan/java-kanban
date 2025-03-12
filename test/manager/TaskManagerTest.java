package manager;

import org.junit.jupiter.api.Test;
import task.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected SubTask subTask;
    protected EpicTask epicTask;

    protected void init() {
        task = new Task(1, "testTask", "taskTest", LocalDateTime.now(), 15);
        epicTask = new EpicTask(2, "epicTest", "testEpic");
        subTask = new SubTask(3, "subTest", "testSub", 2, task.getEndTime(), 10);
        taskManager.addNewTask(task);
        taskManager.addNewEpic(epicTask);
        taskManager.addNewSubTask(subTask);
    }

    @Test
    void getTasksTest() {
        List<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size(), "Метод getTasks работает некорректно");
    }

    @Test
    void getSubTasksTest() {
        List<SubTask> subTasks = taskManager.getSubTasks();
        assertEquals(1, subTasks.size(), "Метод getSubTasks работает некорректно");
    }

    @Test
    void getEpicTasksTest() {
        List<EpicTask> epicTasks = taskManager.getEpicTasks();
        assertEquals(1, epicTasks.size(), "Метод getEpicTasks работает некорректно");
    }

    @Test
    void addSubtaskInSubtaskTest() {
        SubTask subTask2 = new SubTask("subTask2", "testSubTask2", subTask.getId(),
                LocalDateTime.now().plusDays(1), 10);
        taskManager.addNewSubTask(subTask2);
        assertNull(taskManager.getEpicTask(subTask.getId()));
    }

    @Test
    void addDifferentTaskAndSearchForIdTest() {
        assertNotNull(taskManager.getEpicTask(epicTask.getId()));
        assertNotNull(taskManager.getSubTask(subTask.getId()));
        assertNotNull(taskManager.getTask(task.getId()));
    }

    @Test
    void managerDoNotChangeTaskTest() {
        Task task2 = taskManager.getTask(task.getId());
        assertEquals(task.getName(), task2.getName());
        assertEquals(task.getDescription(), task2.getDescription());
    }

    @Test
    void deleteTaskFromMapTest() {
        assertNotNull(taskManager.getTask(task.getId()));
        taskManager.deleteTask(task.getId());
        assertNull(taskManager.getTask(task.getId()));
    }

    @Test
    void deleteSubTaskFromMapTest() {
        assertNotNull(taskManager.getSubTask(subTask.getId()));
        taskManager.deleteSubTask(subTask.getId());
        assertNull(taskManager.getSubTask(subTask.getId()));
    }

    @Test
    void deleteEpicFromMapTest() {
        assertNotNull(taskManager.getEpicTask(epicTask.getId()));
        taskManager.deleteEpic(epicTask.getId());
        assertNull(taskManager.getEpicTask(epicTask.getId()));
    }

    @Test
    void updateTaskTest() {
        Task newTask = new Task(task.getId(), "name2", "newDescription",
                task.getStartTime(), task.getDuration().toMinutes());
        newTask.setStatus(TaskStatus.DONE);
        taskManager.updateTask(newTask);
        assertEquals("name2", taskManager.getTask(task.getId()).getName());
        assertEquals("newDescription", taskManager.getTask(task.getId()).getDescription());
        assertEquals(TaskStatus.DONE, taskManager.getTask(task.getId()).getStatus());
    }

    @Test
    void updateSubTaskTest() {
        SubTask newSubTask = new SubTask(subTask.getId(), "newSub", "updateTest", subTask.getIdOfEpic(),
                subTask.getStartTime(), subTask.getDuration().toMinutes());
        taskManager.updateSubTask(newSubTask);
        assertEquals("newSub", taskManager.getSubTask(subTask.getId()).getName());
        assertEquals("updateTest", taskManager.getSubTask(subTask.getId()).getDescription());
    }

    @Test
    void updateEpicTest() {
        EpicTask newEpic = new EpicTask(epicTask.getId(), "newEpic", "updateTest");
        taskManager.updateEpic(newEpic);
        assertEquals("newEpic", taskManager.getEpicTask(epicTask.getId()).getName());
        assertEquals("updateTest", taskManager.getEpicTask(epicTask.getId()).getDescription());
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

    @Test
    void getGetEpicSubtasksTest() {
        assertEquals(1, taskManager.getEpicSubtasks(epicTask.getId()).size());
    }

    @Test
    void getHistoryOfTaskManagerTest() {
        taskManager.getTask(task.getId());
        taskManager.getSubTask(subTask.getId());
        assertEquals(2, taskManager.getHistory().size());
    }

    @Test
    void deleteSubtaskFromEpicTest() {
        int subId = subTask.getId();
        taskManager.deleteSubTask(subId);
        ArrayList<Integer> subIds = epicTask.getSubTasksIds();
        assertFalse(subIds.contains(subId), "Ошибка, id все еще внутри");
    }

    @Test
    void updateEpicStatusTest() {
        assertEquals(TaskStatus.NEW, epicTask.getStatus(), "Статус рассчитан неверно");
        SubTask subtaskDone = new SubTask("newSub", "subDone", epicTask.getId(), subTask.getEndTime(), 15);
        subtaskDone.setStatus(TaskStatus.DONE);
        taskManager.addNewSubTask(subtaskDone);
        assertEquals(TaskStatus.IN_PROGRESS, epicTask.getStatus(), "Статус рассчитан неверно");
        subTask.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask);
        assertEquals(TaskStatus.DONE, epicTask.getStatus(), "Статус рассчитан неверно");
        subtaskDone.setStatus(TaskStatus.IN_PROGRESS);
        subTask.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subTask);
        taskManager.updateSubTask(subtaskDone);
        assertEquals(TaskStatus.IN_PROGRESS, epicTask.getStatus(), "Статус рассчитан неверно");
    }
}
