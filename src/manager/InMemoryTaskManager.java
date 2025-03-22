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
    protected TreeSet<Task> timeSortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

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
    public int addNewTask(Task task) throws TaskValidationException {
        if (timeSortedTasks.stream().allMatch(oldTask -> isTaskTimeNotCross(task, oldTask))) {
            int id = ++countId;
            task.setId(id);
            taskMap.put(id, task);
            timeSortedTasks.add(task);
            return id;
        } else {
            throw new TaskValidationException();
        }
    }

    @Override
    public int addNewSubTask(SubTask subTask) throws TaskValidationException {
        if (timeSortedTasks.stream().allMatch(oldTask -> isTaskTimeNotCross(subTask, oldTask))) {

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


            timeSortedTasks.add(subTask);
            return id;
        } else {
            throw new TaskValidationException();
        }

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
            Task oldTask = taskMap.get(task.getId());
            timeSortedTasks.remove(oldTask);

            if (timeSortedTasks.stream().allMatch(taskInSet -> isTaskTimeNotCross(task, taskInSet))) {
                taskMap.put(task.getId(), task);
                timeSortedTasks.add(task);
            } else {
                timeSortedTasks.add(oldTask);
            }

        } else {
            System.out.println("There is no such task");
        }

    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTaskMap.containsValue(subTask)) {
            SubTask oldSubTask = subTaskMap.get(subTask.getId());
            timeSortedTasks.remove(oldSubTask);
            if (timeSortedTasks.stream().allMatch(oldTask -> isTaskTimeNotCross(subTask, oldTask))) {
                subTaskMap.put(subTask.getId(), subTask);
                timeSortedTasks.add(subTask);
                updateEpicParameters(epicMap.get(subTask.getIdOfEpic()));
            } else {
                timeSortedTasks.add(oldSubTask);
            }
        } else {
            System.out.println("There is no such subTask");
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

    @Override
    public List<Task> getPrioritizedTasks() {
        return timeSortedTasks.stream().toList();
    }

    private boolean isTaskTimeNotCross(Task task1, Task task2) {

        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }

        return !task1.getStartTime().isBefore(task2.getEndTime()) ||
                !task1.getEndTime().isAfter(task2.getStartTime());
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
                .sorted(Comparator.comparing(Task::getStartTime))
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
