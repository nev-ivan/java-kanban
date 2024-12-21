package manager.tasks;

import type.tasks.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TaskManager {

    protected int countId = 0;

    protected HashMap<Integer, Task> taskMap = new HashMap<>();

    protected HashMap<Integer, EpicTask> epicMap = new HashMap<>();
    protected HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    public int getCountId() {
        return countId;
    }

    public void setCountId(int countId) {
        this.countId = countId;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<Task>(taskMap.values());
    }

    public ArrayList <SubTask> getSubTasks() {
        return new ArrayList<SubTask>(subTaskMap.values());
    }

    public ArrayList <EpicTask> getEpicTasks() {
        return new ArrayList<EpicTask>(epicMap.values());
    }

    public void deleteAllTasks() {
        taskMap.clear();
    }

    public void deleteAllSubTasks() {
        ArrayList<SubTask> subTasks = new ArrayList<SubTask>(subTaskMap.values());
        for (SubTask sub : subTasks) {
            EpicTask epic = epicMap.get(sub.getIdOfEpic());
            epic.getSubTasksIds().clear();
            updateEpicStatus(epic);
        }
        subTaskMap.clear();
    }

    public void deleteAllEpic() {
        epicMap.clear();
        subTaskMap.clear();
    }

    public Task getTask (int id) {
       return taskMap.get(id);
    }

    public SubTask getSubTask (int id) {
        return subTaskMap.get(id);
    }

    public EpicTask getEpicTask (int id) {
        return epicMap.get(id);
    }

    public int addNewTask (Task task) {
        int id = ++countId;
        task.setId(id);
        taskMap.put(id, task);
        return id;
    }

    public int addNewSubTask (SubTask subTask) {

        if (!epicMap.containsKey(subTask.getIdOfEpic())) {
            System.out.println("There is no such epic");
            return -1;
        }

        EpicTask epic = getEpicTask(subTask.getIdOfEpic());
        int id = ++countId;
        subTask.setId(id);
        subTaskMap.put(id, subTask);

        epic.getSubTasksIds().add(id);
        updateEpicStatus(epic);
        return id;
    }

    public int addNewEpic (EpicTask epicTask) {
        int id = ++countId;
        epicTask.setId(id);
        epicMap.put(id, epicTask);
        updateEpicStatus(epicTask);
        return id;
    }

    public void updateTask (Task task) {
        if (taskMap.containsValue(task)) {
            taskMap.put(task.getId(), task);
        } else {
            System.out.println("There is no such task");
        }
    }

    public void updateSubTask (SubTask subTask) {
        if (subTaskMap.containsValue(subTask)) {
            subTaskMap.put(subTask.getId(), subTask);
        } else {
            System.out.println("There is no such subTask");
        }
    }

    public void updateEpic (EpicTask epicTask) {
        if (epicMap.containsValue(epicTask)) {
            epicMap.put(epicTask.getId(), epicTask);
            updateEpicStatus(epicTask);
        } else {
            System.out.println("There is no such epic");
        }

    }

    public void deleteTask(int id) {
        taskMap.remove(id);

    }

    public void deleteSubTask(int id) {
        SubTask subTask = subTaskMap.get(id);
        EpicTask epicTask = epicMap.get(subTask.getIdOfEpic());
        subTaskMap.remove(id);
        epicTask.getSubTasksIds().remove(Integer.valueOf(id));
        updateEpicStatus(epicTask);

    }

    public void deleteEpic(int id) {
        EpicTask epicForDelete = epicMap.get(id);
        for (int subId : epicForDelete.getSubTasksIds()) {
            subTaskMap.remove(subId);
        }
        epicMap.remove(id);
    }

    public ArrayList<SubTask> subTasksOfEpic(int epicId) {
        EpicTask epic = epicMap.get(epicId);
        ArrayList<Integer> subTasksId = epic.getSubTasksIds();
        ArrayList<SubTask> subTasksOfEpic = new ArrayList<>();
        for (int id : subTasksId) {
            subTasksOfEpic.add(subTaskMap.get(id));
        }
        return subTasksOfEpic;
    }

    private void updateEpicStatus (EpicTask epicTask){
        ArrayList<Integer> subTasksId = epicTask.getSubTasksIds();
        Set<TaskStatus> allStatusOfEpic = new HashSet<>();
        for (int id : subTasksId){
           SubTask subTask = subTaskMap.get(id);
           allStatusOfEpic.add(subTask.getStatus());
        }
        if (allStatusOfEpic.isEmpty()||(allStatusOfEpic.contains(TaskStatus.NEW) && allStatusOfEpic.size()==1)) {
            epicTask.setStatus(TaskStatus.NEW);
        } else if (allStatusOfEpic.contains(TaskStatus.DONE) && (allStatusOfEpic.size() == 1)) {
            epicTask.setStatus(TaskStatus.DONE);
        } else {
            epicTask.setStatus(TaskStatus.IN_PROGRESS);
        }
    }


    public HashMap<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public void setTaskMap(HashMap<Integer, Task> taskMap) {
        this.taskMap = taskMap;
    }

    public HashMap<Integer, EpicTask> getEpicMap() {
        return epicMap;
    }

    public void setEpicMap(HashMap<Integer, EpicTask> epicMap) {
        this.epicMap = epicMap;
    }

    public HashMap<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    public void setSubTaskMap(HashMap<Integer, SubTask> subTaskMap) {
        this.subTaskMap = subTaskMap;
    }
}
