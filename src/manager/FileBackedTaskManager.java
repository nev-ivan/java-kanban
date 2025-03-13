package manager;

import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static manager.CSVTaskFormat.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private static final String firstLine = "id,type,name,status,description,epic";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        int counterId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.isBlank()) {
                    break;
                }
                Task task = taskFromString(line);
                taskManager.addTask(task);
                if (task.getId() > counterId) {
                    counterId = task.getId();
                }
            }
            taskManager.countId = counterId;

            String line = reader.readLine();
            if (line != null) {
                List<Integer> history = historyFromString(line);
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
            for (EpicTask epicTask : epicMap.values()) {
                writer.write(epicTask.toString());
            }
            for (SubTask subTask : subTaskMap.values()) {
                writer.write(subTask.toString());
            }
            writer.write(" \n");
            writer.write(historyToString(getHistory()));

        } catch (IOException e) {
            System.out.println("Произошла ошибка записи файла.");
        }
    }

    private void addTask(Task task) {
        if (task != null) {
            switch (task.getType()) {
                case TASK:
                    taskMap.put(task.getId(), task);
                    timeSortedTasks.add(task);
                    break;
                case SUBTASK:
                    subTaskMap.put(task.getId(), (SubTask) task);
                    epicMap.get(((SubTask) task).getIdOfEpic()).addSubtaskId(task.getId());
                    timeSortedTasks.add(task);
                    break;
                case EPIC:
                    epicMap.put(task.getId(), (EpicTask) task);
            }
        }
    }
}
