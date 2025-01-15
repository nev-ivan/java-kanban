package Managers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    protected List<Task> historyView = new ArrayList<>();


    @Override
    public void addTaskInHistory(Task task) {
        if (historyView.size() >= 10) {
            historyView.remove(0);
            historyView.add(task);
        } else {
            historyView.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyView;
    }
}
