package task;

import java.util.ArrayList;

public class EpicTask extends Task {

    protected ArrayList<Integer> subTasksIds = new ArrayList<>();

    public EpicTask(int id, String name, String taskDescription) {
        super(id, name, taskDescription);
    }

    public EpicTask(String name, String taskDescription) {
        super(name, taskDescription);
        status = TaskStatus.NEW;
    }

    public void addSubtaskId(int id) {
        subTasksIds.add(id);
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void clearSubTaskIds() {
        subTasksIds.clear();
    }

    public void deleteSubTask(int id) {
        subTasksIds.remove(Integer.valueOf(id));
    }

    public void setSubTasksIds(ArrayList<Integer> subTasksIds) {
        this.subTasksIds = subTasksIds;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s%n", id, getType(), name, status, taskDescription);
    }
}