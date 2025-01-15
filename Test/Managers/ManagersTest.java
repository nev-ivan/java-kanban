package Managers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTest {

    @Test
    void addTaskManagerNotNull() {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager);
    }

    @Test
    void addHistoryManagerNotNull() {
        HistoryManager manager = Managers.getDefaultHistoryManager();
        assertNotNull(manager);
    }
}
