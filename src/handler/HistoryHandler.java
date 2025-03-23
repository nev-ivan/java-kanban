package handler;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            final List<Task> history = manager.getHistory();
            String response = gson.toJson(history);
            sendText(exchange, response);
        } else {
            sendBadRequest(exchange);
        }
    }
}
