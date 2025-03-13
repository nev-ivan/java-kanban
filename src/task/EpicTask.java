package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicTask extends Task {

    protected LocalDateTime endTime;
    protected ArrayList<Integer> subTasksIds = new ArrayList<>();

    public EpicTask(int id, String name, String description, LocalDateTime startTime, Long duration) {
        super(id, name, description, startTime, duration);
    }

    public EpicTask(int id, String name, String description) {
        super(id, name, description);
        status = TaskStatus.NEW;
        startTime = LocalDateTime.now();
        duration = Duration.ofMinutes(10);
    }

    public EpicTask(String name, String description) {
        super(0, name, description);
        status = TaskStatus.NEW;
        startTime = LocalDateTime.now();
        duration = Duration.ofMinutes(10);
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

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s%n",
                id, getType(), name, status, description, startTime, duration.toMinutes());
    }
}