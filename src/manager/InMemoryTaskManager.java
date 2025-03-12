package manager;

import task.EpicTask;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {

    protected int countId = 0;
    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, EpicTask> epicMap = new HashMap<>();
    protected HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistoryManager();
    Comparator<Task> timeComparator = new TimeComparator();
    protected TreeSet<Task> timeSortedTasks = new TreeSet<>(timeComparator);

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
        for (Integer taskId : taskMap.keySet()) {
            historyManager.remove(taskId);
        }
        taskMap.values().stream().map(task -> timeSortedTasks.remove(task));
        taskMap.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Integer subtaskId : subTaskMap.keySet()) {
            historyManager.remove(subtaskId);
        }
        for (EpicTask epic : epicMap.values()) {
            epic.clearSubTaskIds();
            updateEpicParameters(epic);
        }
        subTaskMap.values().stream().map(task -> timeSortedTasks.remove(task));
        subTaskMap.clear();
    }

    @Override
    public void deleteAllEpic() {
        for (Integer subtaskId : subTaskMap.keySet()) {
            historyManager.remove(subtaskId);
        }
        subTaskMap.values().stream().map(task -> timeSortedTasks.remove(task));
        subTaskMap.clear();
        for (Integer epicId : epicMap.keySet()) {
            historyManager.remove(epicId);
        }
        epicMap.values().stream().map(task -> timeSortedTasks.remove(task));
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
        if (timeSortedTasks.stream().allMatch(oldTask -> isTaskTimeNotCross(task, oldTask))) {
            timeSortedTasks.add(task);
        }
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
        updateEpicParameters(epic);

        if (timeSortedTasks.stream().allMatch(oldTask -> isTaskTimeNotCross(subTask, oldTask))) {
            timeSortedTasks.add(subTask);
        }
        return id;
    }

    @Override
    public int addNewEpic(EpicTask epicTask) {
        int id = ++countId;
        epicTask.setId(id);
        epicMap.put(id, epicTask);
        updateEpicParameters(epicTask);
        return id;
    }

    @Override
    public void updateTask(Task task) {
        if (taskMap.containsValue(task)) {
            timeSortedTasks.remove(taskMap.get(task.getId()));
            taskMap.put(task.getId(), task);
        } else {
            System.out.println("There is no such task");
        }

        if (timeSortedTasks.stream().allMatch(oldTask -> isTaskTimeNotCross(task, oldTask))) {
            timeSortedTasks.add(task);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTaskMap.containsValue(subTask)) {
            timeSortedTasks.remove(subTaskMap.get(subTask.getId()));
            subTaskMap.put(subTask.getId(), subTask);
            updateEpicParameters(epicMap.get(subTask.getIdOfEpic()));
        } else {
            System.out.println("There is no such subTask");
        }

        if (timeSortedTasks.stream().allMatch(oldTask -> isTaskTimeNotCross(subTask, oldTask))) {
            timeSortedTasks.add(subTask);
        }
    }

    @Override
    public void updateEpic(EpicTask epicTask) {
        if (epicMap.containsValue(epicTask)) {
            epicMap.put(epicTask.getId(), epicTask);
            updateEpicParameters(epicTask);
        } else {
            System.out.println("There is no such epic");
        }
    }

    @Override
    public void deleteTask(int id) {
        timeSortedTasks.remove(taskMap.get(id));
        taskMap.remove(id);
        historyManager.remove(id);

    }

    @Override
    public void deleteSubTask(int id) {
        timeSortedTasks.remove(subTaskMap.get(id));
        SubTask subTask = subTaskMap.get(id);
        EpicTask epicTask = epicMap.get(subTask.getIdOfEpic());
        epicTask.deleteSubTask(id);
        subTaskMap.remove(id);
        updateEpicParameters(epicTask);
        historyManager.remove(id);

    }

    @Override
    public void deleteEpic(int id) {
        EpicTask epicForDelete = epicMap.get(id);
        for (int subId : epicForDelete.getSubTasksIds()) {
            timeSortedTasks.remove(subTaskMap.get(subId));
            subTaskMap.remove(subId);
            historyManager.remove(subId);
        }
        timeSortedTasks.remove(epicMap.get(id));
        epicMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<SubTask> getEpicSubtasks(int epicId) {
        return epicMap.get(epicId).getSubTasksIds().stream()
                .map(id -> subTaskMap.get(id)).toList();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public TreeSet getPrioritizedTasks() {
        return timeSortedTasks;
    }

    public boolean isTaskTimeNotCross(Task task1, Task task2) {

        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }

        if (task2.getStartTime().isBefore(task1.getStartTime()) &&
                task2.getEndTime().isAfter(task1.getStartTime())) {
            return false;
        } else if (task2.getStartTime().isAfter(task1.getStartTime()) &&
                task2.getStartTime().isBefore(task1.getEndTime())) {
            return false;
        } else {
            return true;
        }
    }

    private void updateEpicParameters(EpicTask epicTask) {
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

        List<SubTask> allSubSort = epicTask.getSubTasksIds().stream()
                .map(subTaskId -> subTaskMap.get(subTaskId))
                .sorted(timeComparator)
                .toList();

        long durationMinutes = 0;
        for (SubTask sub : allSubSort) {
            durationMinutes = durationMinutes + sub.getDuration().toMinutes();
        }

        if (!allSubSort.isEmpty()) {
            epicTask.setStartTime(allSubSort.getFirst().getStartTime());
            epicTask.setEndTime(allSubSort.getLast().getEndTime());
            epicTask.setDuration(Duration.ofMinutes(durationMinutes));
        }
    }
}
