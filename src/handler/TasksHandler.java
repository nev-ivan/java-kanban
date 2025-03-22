package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpTaskServer;
import manager.TaskManager;
import manager.TaskValidationException;
import task.Task;

import java.io.IOException;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final Integer idFromRequest = getIdFromPath(exchange.getRequestURI().getPath());
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (idFromRequest == -1) {
                    final List<Task> tasks = taskManager.getTasks();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получили список всех задач");
                    sendText(exchange, response);
                    return;
                }
                final Task task = taskManager.getTask(idFromRequest);
                if (task != null) {
                    final String response = gson.toJson(task);
                    System.out.println("Получили задачу по id: " + idFromRequest);
                    sendText(exchange, response);
                } else {
                    System.out.println("Задачи с id = " + idFromRequest + " не найдено");
                    sendNotFound(exchange);
                }
                break;

            case "DELETE":
                taskManager.deleteTask(idFromRequest);
                System.out.println("Задача с id = " + idFromRequest + " удалена");
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
                break;

            case "POST":
                String request = readText(exchange);
                final Task taskFromRequest = gson.fromJson(request, Task.class);
                final int id = taskFromRequest.getId();
                if (id > 0) {
                    taskManager.updateTask(taskFromRequest);
                    System.out.println("Обновили задачу id = " + id);
                    exchange.sendResponseHeaders(200, 0);
                    exchange.close();
                } else {
                    try {
                        int addedId = taskManager.addNewTask(taskFromRequest);
                        System.out.println("Создали задачу id = " + addedId);
                        final String response = gson.toJson(taskFromRequest);
                        sendText(exchange, response);
                    } catch (TaskValidationException e) {
                        System.out.println("Задача пересекается с существующей");
                        sendHasInteractions(exchange);
                    }
                }
                break;

            default:
                System.out.println("Неверный формат запроса");
                sendBadRequest(exchange);
        }
    }
}
