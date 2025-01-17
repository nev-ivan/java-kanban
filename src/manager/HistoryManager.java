package manager;

import task.Task;

import java.util.List;

public interface HistoryManager {

    void addTaskInHistory(Task task);

    List<Task> getHistory();
}
