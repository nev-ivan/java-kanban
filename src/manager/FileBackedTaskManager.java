package manager;

import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    String firstLine = "id,type,name,status,description,epic";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        int counterId = 0;
        boolean notBlank = true;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String lineTest = reader.readLine();
            while (reader.ready() && notBlank) {
                String line = reader.readLine();
                if (line.isBlank()) {
                    break;
                }
                Task task = taskManager.taskFromString(line);
                taskManager.addTask(task);
                if (task.getId() > counterId) {
                    counterId = task.getId();
                }
            }
            taskManager.countId = counterId;

            String line = reader.readLine();
            if (line != null) {
                List<Integer> history = taskManager.historyFromString(line);
                for (Integer id : history) {
                    if (taskManager.taskMap.containsKey(id)) {
                        Task task = taskManager.taskMap.get(id);
                        taskManager.historyManager.add(task);
                    } else if (taskManager.subTaskMap.containsKey(id)) {
                        Task task = taskManager.subTaskMap.get(id);
                        taskManager.historyManager.add(task);
                    } else {
                        Task task = taskManager.epicMap.get(id);
                        taskManager.historyManager.add(task);
                    }
                }
            }


        } catch (IOException e) {
            System.out.println("Произошла ошибка чтения файла");
        }

        return taskManager;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public Task getTask(int id) {
        final Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubTask(int id) {
        final SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public EpicTask getEpicTask(int id) {
        final EpicTask epicTask = super.getEpicTask(id);
        save();
        return epicTask;
    }

    @Override
    public int addNewTask(Task task) {
        final int idNewTask = super.addNewTask(task);
        save();
        return idNewTask;
    }

    @Override
    public int addNewSubTask(SubTask subTask) {
        final int idNewSubTask = super.addNewSubTask(subTask);
        save();
        return idNewSubTask;
    }

    @Override
    public int addNewEpic(EpicTask epicTask) {
        final int idNewEpicTask = super.addNewEpic(epicTask);
        save();
        return idNewEpicTask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(EpicTask epicTask) {
        super.updateEpic(epicTask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    private void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(firstLine + "\n");
            for (Task task : taskMap.values()) {
                writer.write(task.toString());
            }

            for (SubTask subTask : subTaskMap.values()) {
                writer.write(subTask.toString());
            }

            for (EpicTask epicTask : epicMap.values()) {
                writer.write(epicTask.toString());
            }

            writer.write(" \n");
            writer.write(historyToString(getHistory()));

        } catch (IOException e) {
            System.out.println("Произошла ошибка записи файла.");
        }
    }

    private Task taskFromString(String line) {
        String[] taskInArray = line.split(",");
        Task task;
        switch (taskInArray[1]) {
            case "SUBTASK":
                task = new SubTask(Integer.parseInt(taskInArray[0]), taskInArray[2].trim(), taskInArray[4].trim(),
                        Integer.parseInt(taskInArray[5]));
                if (taskInArray[3].equals("IN_PROGRESS")) {
                    task.setStatus(TaskStatus.IN_PROGRESS);
                } else if (taskInArray[3].equals("DONE")) {
                    task.setStatus(TaskStatus.DONE);
                }
                break;
            case "TASK":
                task = new Task(Integer.parseInt(taskInArray[0]), taskInArray[2].trim(), taskInArray[4].trim());
                if (taskInArray[3].equals("IN_PROGRESS")) {
                    task.setStatus(TaskStatus.IN_PROGRESS);
                } else if (taskInArray[3].equals("DONE")) {
                    task.setStatus(TaskStatus.DONE);
                }
                break;
            case "EPIC":
                task = new EpicTask(Integer.parseInt(taskInArray[0]), taskInArray[2].trim(), taskInArray[4].trim());
                if (taskInArray[3].equals("IN_PROGRESS")) {
                    task.setStatus(TaskStatus.IN_PROGRESS);
                } else if (taskInArray[3].equals("DONE")) {
                    task.setStatus(TaskStatus.DONE);
                }
                break;
            default:
                return null;
        }
        return task;
    }

    private void addTask(Task task) {
        if (task != null) {
            switch (task.getType()) {
                case TASK -> taskMap.put(task.getId(), task);
                case SUBTASK -> subTaskMap.put(task.getId(), (SubTask) task);
                case EPIC -> epicMap.put(task.getId(), (EpicTask) task);
            }
        }
    }

    private List<Integer> historyFromString(String line) {
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

    private String historyToString(List<Task> history) {
        StringBuilder line = new StringBuilder();
        for (Task task : history) {
            line.append(task.getId() + ",");
        }
        return line.toString();
    }
}
