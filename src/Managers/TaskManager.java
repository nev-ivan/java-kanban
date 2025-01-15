package Managers;

import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<SubTask> getSubTasks();

    ArrayList<EpicTask> getEpicTasks();

    void deleteAllTasks();

    void deleteAllSubTasks();

    void deleteAllEpic();

    Task getTask(int id);

    SubTask getSubTask(int id);

    EpicTask getEpicTask(int id);

    int addNewTask(Task task);

    int addNewSubTask(SubTask subTask);

    int addNewEpic(EpicTask epicTask);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(EpicTask epicTask);

    void deleteTask(int id);

    void deleteSubTask(int id);

    void deleteEpic(int id);

    ArrayList<SubTask> subTasksOfEpic(int epicId);

    List<Task> getHistory();
}
