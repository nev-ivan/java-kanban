import java.util.ArrayList;

public class EpicTask extends Task {

    protected ArrayList <Integer> subTasksIds = new ArrayList<>();


    public EpicTask (int id, String name, String taskDescription, TaskStatus status) {
        super(id, name, taskDescription, status);
    }

    public EpicTask (String name, String taskDescription, TaskStatus status) {
        super(name, taskDescription, status);
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
}
