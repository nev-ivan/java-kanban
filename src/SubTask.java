public class SubTask extends Task {

    protected int idOfEpic;

    public SubTask(String name, String taskDescription, TaskStatus status, int IdOfEpic) {
        super(name, taskDescription, status);
        this.idOfEpic = IdOfEpic;
    }

    public SubTask(int id, String name, String taskDescription, TaskStatus status, int IdOfEpic) {
        super(id, name, taskDescription, status);
        this.idOfEpic = IdOfEpic;
    }

    public int getIdOfEpic() {
        return idOfEpic;
    }

}

