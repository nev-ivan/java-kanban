package manager;

import task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InMemoryTaskManager implements TaskManager {

    protected int countId = 0;
    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, EpicTask> epicMap = new HashMap<>();
    protected HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistoryManager();

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTaskMap.values());
    }

    @Override
    public ArrayList<EpicTask> getEpicTasks() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Map.Entry<Integer, Task> taskEntry : taskMap.entrySet()) {
            historyManager.remove(taskEntry.getKey());
        }
        taskMap.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Map.Entry<Integer, SubTask> subTaskEntry : subTaskMap.entrySet()) {
            EpicTask epic = epicMap.get(subTaskEntry.getValue().getIdOfEpic());
            epic.clearSubTaskIds();
            updateEpicStatus(epic);
            historyManager.remove(subTaskEntry.getKey());
        }
        subTaskMap.clear();
    }

    @Override
    public void deleteAllEpic() {
        deleteAllSubTasks();
        for (Map.Entry<Integer, EpicTask> epicTaskEntry : epicMap.entrySet()) {
            historyManager.remove(epicTaskEntry.getKey());
        }
        epicMap.clear();
    }

    @Override
    public Task getTask(int id) {
        Task task = taskMap.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTaskMap.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public EpicTask getEpicTask(int id) {
        EpicTask epicTask = epicMap.get(id);
        historyManager.add(epicTask);
        return epicTask;
    }

    @Override
    public int addNewTask(Task task) {
        int id = ++countId;
        task.setId(id);
        taskMap.put(id, task);
        return id;
    }

    @Override
    public int addNewSubTask(SubTask subTask) {

        if (!epicMap.containsKey(subTask.getIdOfEpic())) {
            System.out.println("There is no such epic");
            return -1;
        }

        EpicTask epic = epicMap.get(subTask.getIdOfEpic());
        int id = ++countId;
        subTask.setId(id);
        subTaskMap.put(id, subTask);

        epic.addSubtaskId(id);
        updateEpicStatus(epic);
        return id;
    }

    @Override
    public int addNewEpic(EpicTask epicTask) {
        int id = ++countId;
        epicTask.setId(id);
        epicMap.put(id, epicTask);
        updateEpicStatus(epicTask);
        return id;
    }

    @Override
    public void updateTask(Task task) {
        if (taskMap.containsValue(task)) {
            taskMap.put(task.getId(), task);
        } else {
            System.out.println("There is no such task");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTaskMap.containsValue(subTask)) {
            subTaskMap.put(subTask.getId(), subTask);
            updateEpicStatus(epicMap.get(subTask.getIdOfEpic()));
        } else {
            System.out.println("There is no such subTask");
        }
    }

    @Override
    public void updateEpic(EpicTask epicTask) {
        if (epicMap.containsValue(epicTask)) {
            epicMap.put(epicTask.getId(), epicTask);
            updateEpicStatus(epicTask);
        } else {
            System.out.println("There is no such epic");
        }

    }

    @Override
    public void deleteTask(int id) {
        taskMap.remove(id);
        historyManager.remove(id);

    }

    @Override
    public void deleteSubTask(int id) {
        SubTask subTask = subTaskMap.get(id);
        EpicTask epicTask = epicMap.get(subTask.getIdOfEpic());
        epicTask.deleteSubTask(id);
        subTaskMap.remove(id);
        updateEpicStatus(epicTask);
        historyManager.remove(id);

    }

    @Override
    public void deleteEpic(int id) {
        EpicTask epicForDelete = epicMap.get(id);
        for (int subId : epicForDelete.getSubTasksIds()) {
            subTaskMap.remove(subId);
            historyManager.remove(subId);
        }
        epicMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public ArrayList<SubTask> subTasksOfEpic(int epicId) {
        EpicTask epic = epicMap.get(epicId);
        ArrayList<Integer> subTasksId = epic.getSubTasksIds();
        ArrayList<SubTask> subTasksOfEpic = new ArrayList<>();
        for (int id : subTasksId) {
            subTasksOfEpic.add(subTaskMap.get(id));
        }
        return subTasksOfEpic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(EpicTask epicTask) {
        ArrayList<Integer> subTasksId = epicTask.getSubTasksIds();
        Set<TaskStatus> allStatusOfEpic = new HashSet<>();
        for (int id : subTasksId) {
            SubTask subTask = subTaskMap.get(id);
            allStatusOfEpic.add(subTask.getStatus());
        }
        if (allStatusOfEpic.isEmpty() || (allStatusOfEpic.contains(TaskStatus.NEW) && allStatusOfEpic.size() == 1)) {
            epicTask.setStatus(TaskStatus.NEW);
        } else if (allStatusOfEpic.contains(TaskStatus.DONE) && (allStatusOfEpic.size() == 1)) {
            epicTask.setStatus(TaskStatus.DONE);
        } else {
            epicTask.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
