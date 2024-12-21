package type.tasks;

import java.util.ArrayList;

public class EpicTask extends Task {

    protected ArrayList <Integer> subTasksIds = new ArrayList<>();


    public EpicTask (int id, String name, String taskDescription) {
        super(id, name, taskDescription);
        status = TaskStatus.NEW;
    }

    public EpicTask (String name, String taskDescription) {
        super(name, taskDescription);
        status = TaskStatus.NEW;
    }

    public void addSubtaskId (int id) {
        subTasksIds.add(id);
    }

    public ArrayList <Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void clearSubTaskIds() {
        subTasksIds.clear();
    }

    public void deleteSubTask(int id) {
        subTasksIds.remove(id);
    }

    public void setSubTasksIds(ArrayList<Integer> subTasksIds) {
        this.subTasksIds = subTasksIds;
    }
}
