package manager;

import task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
        init();
    }

    @Test
    void crossTimeInTasksTest() {
        Task task1 = new Task("task2", "crossTimeTask", subTask.getEndTime().minusMinutes(5), 20);
        assertFalse(taskManager.isTaskTimeNotCross(subTask, task1));
        assertTrue(taskManager.isTaskTimeNotCross(task, task1));
    }

}
