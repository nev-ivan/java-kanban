package type.tasks;

import java.util.Objects;

public class Task {

    protected String name;
    protected String taskDescription;
    protected int id;
    protected TaskStatus status;

    public Task (int id, String name, String taskDescription) {
        this(name, taskDescription);
        this.id = id;
        status = TaskStatus.NEW;
    }

    public Task (String name, String taskDescription) {
        this.name = name;
        this.taskDescription = taskDescription;
        status = TaskStatus.NEW;
    }

    public String getName() {
        return name;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
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
        return Objects.equals(id, task.id);
    }



    @Override
    public String toString () {
        return "name=" + name + ", taskDescription=" + taskDescription + ", status=" + status;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
