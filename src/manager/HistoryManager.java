package manager;

import task.Task;

import java.util.List;

public interface HistoryManager {

    <T extends Task> T addTaskInHistory(T task);

    List<Task> getHistory();
}
