import manager.tasks.TaskManager;
import type.tasks.EpicTask;
import type.tasks.SubTask;
import type.tasks.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        Task task1 = new Task("Завтрак", "Готовим на завтрак яичницу");
        Task task2 = new Task("Занятия спортом", "Отжимаемся от пола 50 раз");
        manager.addNewTask(task1);
        manager.addNewTask(task2);

        EpicTask epic1 = new EpicTask("Уборка", "Уборка в квартире");
        EpicTask epic2 = new EpicTask("Обслуживание компьютера", "Разборка и чистка системного блока ПК");
        manager.addNewEpic(epic2);
        manager.addNewEpic(epic1);

        SubTask subTask1 = new SubTask("Разборка", "Снятия крышки ПК отверткой", epic2.getId());
        SubTask subTask2 = new SubTask("Чистка", "Собирание пыли инструментами внутри ПК", epic2.getId());
        SubTask subTask3 = new SubTask("Собираем вещи", "Собрать грязные вещи в стиралку", epic1.getId());
        manager.addNewSubTask(subTask1);
        manager.addNewSubTask(subTask2);
        manager.addNewSubTask(subTask3);

        System.out.println(manager.getTaskMap());
        System.out.println(manager.getEpicMap());
        System.out.println(manager.getSubTaskMap());

        manager.deleteSubTask(subTask3.getId());
        manager.deleteTask(task1.getId());

        System.out.println(manager.getTaskMap());
        System.out.println(manager.getEpicMap());
        System.out.println(manager.getSubTaskMap());
    }
}
