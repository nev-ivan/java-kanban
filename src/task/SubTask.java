package task;

import java.time.LocalDateTime;

public class SubTask extends Task {

    protected int idOfEpic;

    public SubTask(String name, String taskDescription, int idOfEpic,
                   LocalDateTime startTime, long durationMinutes) {
        super(name, taskDescription, startTime, durationMinutes);
        this.idOfEpic = idOfEpic;
    }

    public SubTask(int id, String name, String taskDescription, int idOfEpic,
                   LocalDateTime startTime, long durationMinutes) {
        super(id, name, taskDescription, startTime, durationMinutes);
        this.idOfEpic = idOfEpic;
        status = TaskStatus.NEW;
    }

    public int getIdOfEpic() {
        return idOfEpic;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s%n",
                id, getType(), name, status, description, startTime, duration.toMinutes(), idOfEpic);
    }
}
