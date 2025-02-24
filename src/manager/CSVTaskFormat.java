package manager;

import task.*;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormat {
    public static List<Integer> historyFromString(String line) {
        if (line == null) {
            return null;
        }
        List<Integer> tasksId = new ArrayList<>();
        String[] idInStringArray = line.split(",");
        for (String s : idInStringArray) {
            tasksId.add(Integer.parseInt(s));
        }
        return tasksId;
    }

    public static String historyToString(List<Task> history) {
        StringBuilder line = new StringBuilder();
        for (Task task : history) {
            line.append(task.getId() + ",");
        }
        return line.toString();
    }

    public static Task taskFromString(String line) {
        final String[] taskInArray = line.split(",");
        final int id = Integer.parseInt(taskInArray[0]);
        final TaskType type = TaskType.valueOf(taskInArray[1]);
        final String name = taskInArray[2].trim();
        final TaskStatus status = TaskStatus.valueOf(taskInArray[3]);
        final String description = taskInArray[4].trim();
        Task task;
        switch (type) {
            case SUBTASK:
                int idOfEpic = Integer.parseInt(taskInArray[5]);
                task = new SubTask(id, name, description, idOfEpic);
                break;
            case TASK:
                task = new Task(id, name, description);
                break;
            case EPIC:
                task = new EpicTask(id, name, description);
                break;
            default:
                return null;
        }
        task.setStatus(status);
        return task;
    }
}
