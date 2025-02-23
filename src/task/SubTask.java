package task;

public class SubTask extends Task {

    protected int idOfEpic;

    public SubTask(String name, String taskDescription, int idOfEpic) {
        super(name, taskDescription);
        this.idOfEpic = idOfEpic;
        status = TaskStatus.NEW;
        type = TaskType.SUBTASK;
    }

    public SubTask(int id, String name, String taskDescription, int idOfEpic) {
        super(id, name, taskDescription);
        this.idOfEpic = idOfEpic;
        status = TaskStatus.NEW;
        type = TaskType.SUBTASK;
    }

    public int getIdOfEpic() {
        return idOfEpic;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s%n", id, type, name, status, taskDescription, idOfEpic);
    }
}
