import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TaskManager {

    ArrayList <Integer> tasksIdList = new ArrayList<>();
    int countId = 0;

    HashMap<Integer, Task> taskMap = new HashMap<>();
    HashMap<Integer, EpicTask> epicMap = new HashMap<>();
    HashMap<Integer, SubTask> subTaskMap = new HashMap<>();

    public TaskManager () {}

    public ArrayList<Task> getTasks() {
        ArrayList <Task> tasks = new ArrayList<>();
        for (Integer id : tasksIdList) {
            if (taskMap.containsKey(id)) {
                tasks.add(taskMap.get(id));
            }
        }
        return tasks;
    }

    public ArrayList <SubTask> getSubTasks() {
        ArrayList <SubTask> subTasks = new ArrayList<>();
        for (Integer id : tasksIdList) {
            if (subTaskMap.containsKey(id)) {
                subTasks.add(subTaskMap.get(id));
            }
        }
        return subTasks;
    }

    public ArrayList <EpicTask> getEpicTasks() {
        ArrayList <EpicTask> epicTasks = new ArrayList<>();
        for (Integer id : tasksIdList) {
            if (epicMap.containsKey(id)) {
                epicTasks.add(epicMap.get(id));
            }
        }
        return epicTasks;
    }

    void deleteAllTasks() {
        taskMap.clear();
    }

    void deleteAllSubTasks() {
        subTaskMap.clear();
    }

    void deleteAllEpic() {
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

    int addNewTask (Task task) {
        int id = ++countId;
        task.setId(id);
        taskMap.put(id, task);
        tasksIdList.add(id);
        return id;
    }

    int addNewSubTask (SubTask subTask) {

        if (!epicMap.containsKey(subTask.idOfEpic)) {
            System.out.println("There is no such epic");
            return -1;
        }

        EpicTask epic = getEpicTask(subTask.idOfEpic);
        int id = ++countId;
        subTask.setId(id);
        subTaskMap.put(id, subTask);

        epic.subTasksIds.add(id);
        updateEpicStatus(epic);
        tasksIdList.add(id);
        return id;
    }

    int addNewEpic (EpicTask epicTask) {
        int id = ++countId;
        epicTask.setId(id);
        epicMap.put(id, epicTask);
        updateEpicStatus(epicTask);
        tasksIdList.add(id);
        return id;
    }

    void updateTask (Task task) {
        if (taskMap.containsValue(task)) {
            taskMap.put(task.id, task);
        } else {
            System.out.println("There is no such task");
        }
    }

    void updateSubTask (SubTask subTask) {
        if (subTaskMap.containsValue(subTask)) {
            subTaskMap.put(subTask.id, subTask);
        } else {
            System.out.println("There is no such subTask");
        }
    }

    void updateEpic (EpicTask epicTask) {
        if (epicMap.containsValue(epicTask)) {
            epicMap.put(epicTask.id, epicTask);
            updateEpicStatus(epicTask);
        } else {
            System.out.println("There is no such epic");
        }

    }

    void deleteTask(int id) {
        taskMap.remove(id);

    }

    void deleteSubTask(int id) {
        SubTask subTask = subTaskMap.get(id);
        EpicTask epicTask = epicMap.get(subTask.idOfEpic);
        subTaskMap.remove(id);
        epicTask.subTasksIds.remove(epicTask.subTasksIds.indexOf(id));
        updateEpicStatus(epicTask);

    }

    void deleteEpic(int id) {
        EpicTask epicForDelete = epicMap.get(id);
        for (int subId : epicForDelete.getSubTasksIds()) {
            subTaskMap.remove(subId);
        }
        epicMap.remove(id);
    }

    ArrayList<SubTask> subTasksOfEpic(int epicId) {
        EpicTask epic = epicMap.get(epicId);
        ArrayList<Integer> subTasksId = epic.getSubTasksIds();
        ArrayList<SubTask> subTasksOfEpic = new ArrayList<>();
        for (int id : subTasksId) {
            subTasksOfEpic.add(subTaskMap.get(id));
        }
        return subTasksOfEpic;
    }

    void updateEpicStatus (EpicTask epicTask){
        ArrayList<Integer> subTasksId = epicTask.subTasksIds;
        Set<TaskStatus> allStatusOfEpic = new HashSet<>();
        for (int id : subTasksId){
           SubTask subTask = subTaskMap.get(id);
           allStatusOfEpic.add(subTask.status);
        }
        if (allStatusOfEpic.isEmpty()||(allStatusOfEpic.contains(TaskStatus.NEW) && allStatusOfEpic.size()==1)) {
            epicTask.setStatus(TaskStatus.NEW);
        } else if (allStatusOfEpic.contains(TaskStatus.DONE) && (allStatusOfEpic.size() == 1)) {
            epicTask.setStatus(TaskStatus.DONE);
        } else {
            epicTask.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

}
