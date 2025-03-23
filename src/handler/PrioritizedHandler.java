package handler;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            final List<Task> prioritized = manager.getPrioritizedTasks();
            String response = gson.toJson(prioritized);
            sendText(exchange, response);
        } else {
            sendBadRequest(exchange);
        }
    }
}