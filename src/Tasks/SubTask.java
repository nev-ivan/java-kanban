package Tasks;

public class SubTask extends Task {

    protected int idOfEpic;

    public SubTask(String name, String taskDescription, int idOfEpic) {
        super(name, taskDescription);
        this.idOfEpic = idOfEpic;
    }

    public SubTask(int id, String name, String taskDescription, int idOfEpic) {
        super(id, name, taskDescription);
        status = TaskStatus.NEW;
        this.idOfEpic = idOfEpic;
    }

    public int getIdOfEpic() {
        return idOfEpic;
    }

}
