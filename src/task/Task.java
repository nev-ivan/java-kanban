package task;

import java.util.Objects;

public class Task {

    protected String name;
    protected String taskDescription;
    protected int id;

    protected TaskStatus status;

    public Task(int id, String name, String taskDescription) {
        this.id = id;
        this.name = name;
        this.taskDescription = taskDescription;
        status = TaskStatus.NEW;
    }

    public Task(String name, String taskDescription) {
        this(0, name, taskDescription);
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

    public TaskType getType() {
        return TaskType.TASK;
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
    public String toString() {
        return String.format("%s,%s,%s,%s,%s %n", id, getType(), name, status, taskDescription);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
