package http;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
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

import static org.junit.jupiter.api.Assertions.*;

public class TasksHandlerTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();
    Task task;

    public TasksHandlerTest() throws IOException {
    }

    @BeforeEach
    void beforeEach() {
        manager.deleteAllTasks();
        manager.deleteAllSubTasks();
        manager.deleteAllEpic();
        server.setUp();
        task = new Task("Test 2", "Testing task 2", LocalDateTime.now(), 5);
    }

    @AfterEach
    void afterEach() {
        server.shutDown();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void deleteTaskTest() throws IOException, InterruptedException {
        manager.addNewTask(task);
        assertFalse(manager.getTasks().isEmpty());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getTasks();
        assertTrue(tasksFromManager.isEmpty());
    }

    @Test
    void getTasksTest() throws IOException, InterruptedException {
        Task task2 = new Task("Test 2", "Testing task 2", task.getStartTime().plusDays(1), 5);
        manager.addNewTask(task);
        manager.addNewTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String tasksJson = gson.toJson(manager.getTasks());

        assertEquals(200, response.statusCode(), "Неправильный код ответа");
        assertEquals(tasksJson, response.body(), "Неправильное тело ответа");
    }

    @Test
    void getTaskFromIdTest() throws IOException, InterruptedException {
        int idForRequest = manager.addNewTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + idForRequest);
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String taskJson = gson.toJson(task);

        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertEquals(taskJson, response.body(), "Неверно получена задача в теле ответа");
    }


}
