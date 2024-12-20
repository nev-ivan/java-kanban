import java.util.Objects;

public class Task {
    protected String name;
    protected String taskDescription;

    protected int id;

    protected TaskStatus status;

    int numberOfTask;
    public Task (int id, String name, String taskDescription, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.taskDescription = taskDescription;
        this.status = status;

    }
    public Task (String name, String taskDescription, TaskStatus status) {
        this.name = name;
        this.taskDescription = taskDescription;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals (Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) &&
                Objects.equals(taskDescription, task.taskDescription);
    }

    @Override
   public int hashCode() {
        int hash = 3;

        if (name != null){
            hash = hash + Objects.hash(name);
        }

        hash = hash * 7;

        if (taskDescription != null) {
            hash = hash + Objects.hash(taskDescription);
        }
        return hash;
    }

    @Override
    public String toString () {
        return "name=" + name + ", taskDescription=" + taskDescription + ", status=" + status;
    }
}
