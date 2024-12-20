public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        Task task1 = new Task("Завтрак", "Готовим на завтрак яичницу", TaskStatus.DONE);
        Task task2 = new Task("Занятия спортом", "Отжимаемся от пола 50 раз", TaskStatus.NEW);
        manager.addNewTask(task1);
        manager.addNewTask(task2);

        EpicTask epic1 = new EpicTask("Уборка", "Уборка в квартире", TaskStatus.NEW);
        EpicTask epic2 = new EpicTask("Обслуживание компьютера", "Разборка и чистка системного блока ПК", TaskStatus.NEW);
        manager.addNewEpic(epic2);
        manager.addNewEpic(epic1);

        SubTask subTask1 = new SubTask("Разборка", "Снятия крышки ПК отверткой",TaskStatus.IN_PROGRESS, epic2.id);
        SubTask subTask2 = new SubTask("Чистка", "Собирание пыли инструментами внутри ПК",TaskStatus.NEW, epic2.id);
        SubTask subTask3 = new SubTask("Собираем вещи", "Собрать грязные вещи в стиралку",TaskStatus.IN_PROGRESS, epic1.id);
        manager.addNewSubTask(subTask1);
        manager.addNewSubTask(subTask2);
        manager.addNewSubTask(subTask3);

        System.out.println(manager.taskMap);
        System.out.println(manager.epicMap);
        System.out.println(manager.subTaskMap);

        manager.deleteSubTask(subTask3.id);
        manager.deleteTask(task1.id);

        System.out.println(manager.taskMap);
        System.out.println(manager.epicMap);
        System.out.println(manager.subTaskMap);
    }
}
