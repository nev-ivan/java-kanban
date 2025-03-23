package http;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.TaskValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryHandlerTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();
    public HistoryHandlerTest() throws IOException {
    }

    @BeforeEach
    void beforeEach() {
        manager.deleteAllTasks();
        manager.deleteAllSubTasks();
        manager.deleteAllEpic();
        server.setUp();
    }

    @AfterEach
    void afterEach() {
        server.shutDown();
    }

    @Test
    void getHistoryTest() throws TaskValidationException, IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", LocalDateTime.now(), 5);
        Task task2 = new Task("Test 2", "Testing task 2", task.getStartTime().plusDays(1), 5);
        manager.addNewTask(task);
        manager.addNewTask(task2);
        manager.getTask(task.getId());
        manager.getTask(task2.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> history = manager.getHistory();
        String historyJson = gson.toJson(history);
        assertEquals(200, response.statusCode());
        assertEquals(response.body(), historyJson);
    }
}
