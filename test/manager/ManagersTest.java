package manager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTest {

    @Test
    void addTaskManagerNotNull() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);
    }

    @Test
    void addHistoryManagerNotNull() {
        HistoryManager manager = Managers.getDefaultHistoryManager();
        assertNotNull(manager);
    }
}
