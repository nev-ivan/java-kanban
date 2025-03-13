package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    protected String name;
    protected String description;
    protected int id;
    protected Duration duration;
    protected LocalDateTime startTime;

    protected TaskStatus status;

    public Task(int id, String name, String description, LocalDateTime startTime, long durationMinutes) {
        this.id = id;
        this.name = name;
        this.description = description;
        duration = Duration.ofMinutes(durationMinutes);
        this.startTime = startTime;
        status = TaskStatus.NEW;
    }

    public Task(String name, String description, LocalDateTime startTime, long durationMinutes) {
        this(0, name, description, startTime, durationMinutes);
    }

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        startTime = LocalDateTime.now();
        duration = Duration.ofMinutes(10);
        status = TaskStatus.NEW;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s %n",
                id, getType(), name, status, description, startTime, duration.toMinutes());
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
