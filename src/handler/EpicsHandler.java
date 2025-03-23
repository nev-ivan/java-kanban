package handler;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.EpicTask;
import task.SubTask;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        final Integer idFromRequest = getIdFromPath(exchange.getRequestURI().getPath());
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (splitPath.length == 4) {
                    final List<SubTask> subtasks = manager.getEpicSubtasks(idFromRequest);
                    if (subtasks == null) {
                        System.out.println("Эпика с id = " + idFromRequest + " не найдено");
                        sendNotFound(exchange);
                        return;
                    }
                    String response = gson.toJson(subtasks);
                    System.out.println("Получили подзадачи эпика с id = " + idFromRequest);
                    sendText(exchange, response);
                    return;
                } else if (idFromRequest == -1) {
                    final List<EpicTask> epics = manager.getEpicTasks();
                    final String response = gson.toJson(epics);
                    System.out.println("Получили список всех эпиков");
                    sendText(exchange, response);
                    return;
                } else if (splitPath.length == 3) {
                    final EpicTask epic = manager.getEpicTask(idFromRequest);
                    if (epic == null) {
                        System.out.println("Эпика с id = " + idFromRequest + " не найдено");
                        sendNotFound(exchange);
                        return;
                    }
                    final String response = gson.toJson(epic);
                    System.out.println("Получили эпик по id: " + idFromRequest);
                    sendText(exchange, response);
                }
                break;

            case "DELETE":
                manager.deleteEpic(idFromRequest);
                System.out.println("Эпик с id = " + idFromRequest + " удалён");
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
                break;

            case "POST":
                String request = readText(exchange);
                final EpicTask epicFromRequest = gson.fromJson(request, EpicTask.class);
                final int id = epicFromRequest.getId();
                if (id > 0) {
                    manager.updateEpic(epicFromRequest);
                    System.out.println("Обновили эпик id = " + id);
                    exchange.sendResponseHeaders(200, 0);
                    exchange.close();
                } else {
                    int addedId = manager.addNewEpic(epicFromRequest);
                    System.out.println("Создали эпик id = " + addedId);
                    final String response = gson.toJson(epicFromRequest);
                    sendText(exchange, response);
                }
                break;

            default:
                System.out.println("Неверный формат запроса");
                sendBadRequest(exchange);
        }
    }
}