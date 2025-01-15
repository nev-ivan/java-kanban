package Managers;

import Tasks.*;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int countId = 0;
    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, EpicTask> epicMap = new HashMap<>();
    protected HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    HistoryManager historyManager = new InMemoryHistoryManager();

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<Task>(taskMap.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<SubTask>(subTaskMap.values());
    }

    @Override
    public ArrayList<EpicTask> getEpicTasks() {
        return new ArrayList<EpicTask>(epicMap.values());
    }

    @Override
    public void deleteAllTasks() {
        taskMap.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        ArrayList<SubTask> subTasks = new ArrayList<SubTask>(subTaskMap.values());
        for (SubTask sub : subTasks) {
            EpicTask epic = epicMap.get(sub.getIdOfEpic());
            epic.clearSubTaskIds();
            updateEpicStatus(epic);
        }
        subTaskMap.clear();
    }

    @Override
    public void deleteAllEpic() {
        epicMap.clear();
        subTaskMap.clear();
    }

    @Override
    public Task getTask(int id) {
        historyManager.addTaskInHistory(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        historyManager.addTaskInHistory(subTaskMap.get(id));
        return subTaskMap.get(id);
    }

    @Override
    public EpicTask getEpicTask(int id) {
        historyManager.addTaskInHistory(epicMap.get(id));
        return epicMap.get(id);
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

        EpicTask epic = getEpicTask(subTask.getIdOfEpic());
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

    }

    @Override
    public void deleteSubTask(int id) {
        SubTask subTask = subTaskMap.get(id);
        EpicTask epicTask = epicMap.get(subTask.getIdOfEpic());
        epicTask.deleteSubTask(id);
        subTaskMap.remove(id);
        updateEpicStatus(epicTask);

    }

    @Override
    public void deleteEpic(int id) {
        EpicTask epicForDelete = epicMap.get(id);
        for (int subId : epicForDelete.getSubTasksIds()) {
            subTaskMap.remove(subId);
        }
        epicMap.remove(id);
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
