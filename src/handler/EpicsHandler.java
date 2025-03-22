package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpTaskServer;
import manager.TaskManager;
import task.EpicTask;
import task.SubTask;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        final Integer idFromRequest = getIdFromPath(exchange.getRequestURI().getPath());
        switch (exchange.getRequestMethod()) {
            case "GET":
                final EpicTask epic = taskManager.getEpicTask(idFromRequest);
                if (splitPath.length == 4 && epic != null) {
                    final List<SubTask> subtasks = epic.getSubTasksIds().stream()
                            .map(taskManager::getSubTask)
                            .toList();
                    String response = gson.toJson(subtasks);
                    System.out.println("Получили подзадачи эпика с id = " + idFromRequest);
                    sendText(exchange, response);
                    return;
                } else if (idFromRequest == -1) {
                    final List<EpicTask> epics = taskManager.getEpicTasks();
                    final String response = gson.toJson(epics);
                    System.out.println("Получили список всех эпиков");
                    sendText(exchange, response);
                    return;
                } else if (epic != null) {
                    final String response = gson.toJson(epic);
                    System.out.println("Получили эпик по id: " + idFromRequest);
                    sendText(exchange, response);
                } else {
                    System.out.println("Эпика с id = " + idFromRequest + " не найдено");
                    sendNotFound(exchange);
                }
                break;

            case "DELETE":
                taskManager.deleteEpic(idFromRequest);
                System.out.println("Эпик с id = " + idFromRequest + " удалён");
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
                break;

            case "POST":
                String request = readText(exchange);
                final EpicTask epicFromRequest = gson.fromJson(request, EpicTask.class);
                final int id = epicFromRequest.getId();
                if (id > 0) {
                    taskManager.updateEpic(epicFromRequest);
                    System.out.println("Обновили эпик id = " + id);
                    exchange.sendResponseHeaders(200, 0);
                    exchange.close();
                } else {
                    int addedId = taskManager.addNewEpic(epicFromRequest);
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