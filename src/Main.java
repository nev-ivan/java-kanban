import manager.*;
import task.EpicTask;
import task.SubTask;
import task.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task("1", "11");
        Task task2 = new Task("2", "22");
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        EpicTask epicTask1 = new EpicTask("Епик1", "Епик11");
        EpicTask epicTask2 = new EpicTask("Епик2", "Епик22");
        taskManager.addNewEpic(epicTask1);
        taskManager.addNewEpic(epicTask2);
        SubTask subTask1 = new SubTask("Саб1", "Саб11", epicTask1.getId());
        SubTask subTask2 = new SubTask("Саб2", "Саб22", epicTask1.getId());
        SubTask subTask3 = new SubTask("Саб3", "Саб33", epicTask1.getId());
        taskManager.addNewSubTask(subTask1);
        taskManager.addNewSubTask(subTask2);
        taskManager.addNewSubTask(subTask3);

        taskManager.getSubTask(subTask1.getId());
        taskManager.getSubTask(subTask2.getId());
        taskManager.getSubTask(subTask1.getId());
        taskManager.getSubTask(subTask3.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getEpicTask(epicTask1.getId());
        taskManager.getEpicTask(epicTask2.getId());

        System.out.println(taskManager.getHistory());
        taskManager.deleteEpic(epicTask1.getId());
        System.out.println(taskManager.getHistory());
    }
}
