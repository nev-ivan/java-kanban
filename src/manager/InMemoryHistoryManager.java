package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    protected List<Task> historyView = new ArrayList<>();
    private final static int MAX_SIZE = 10;

    @Override
    public void addTaskInHistory(Task task) {
        if (historyView.size() >= MAX_SIZE) {
            historyView.removeFirst();
        }
        historyView.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyView;
    }
}
