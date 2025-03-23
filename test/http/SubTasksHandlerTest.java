package http;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.SubTask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubTasksHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();
    EpicTask epic;
    SubTask subTask;

    public SubTasksHandlerTest() throws IOException {
    }

    @BeforeEach
    void beforeEach() {
        manager.deleteAllTasks();
        manager.deleteAllSubTasks();
        manager.deleteAllEpic();
        server.setUp();
        epic = new EpicTask("Epic", "Description");
        manager.addNewEpic(epic);
        subTask = new SubTask("Test 2", "Testing subtask 2", epic.getId(), LocalDateTime.now(), 5);
    }

    @AfterEach
    void afterEach() {
        server.shutDown();
    }

    @Test
    public void testAddSubTask() throws IOException, InterruptedException {
        String subtaskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<SubTask> SubtasksFromManager = manager.getSubTasks();

        assertNotNull(SubtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, SubtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", SubtasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void getSubTasksTest() throws IOException, InterruptedException {
        manager.addNewSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String subtasksJson = gson.toJson(manager.getSubTasks());

        assertEquals(200, response.statusCode(), "Неправильный код ответа");
        assertEquals(subtasksJson, response.body(), "Неправильное тело ответа");
    }

    @Test
    void getSubTaskFromIdTest() throws IOException, InterruptedException {
        int idForRequest = manager.addNewSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + idForRequest);
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String subtaskJson = gson.toJson(subTask);

        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertEquals(subtaskJson, response.body(), "Неверно получена задача в теле ответа");
    }

    @Test
    void deleteSubTaskTest() throws IOException, InterruptedException {
        int idForRequest = manager.addNewSubTask(subTask);
        assertFalse(manager.getSubTasks().isEmpty());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + idForRequest);
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<SubTask> subtasksFromManager = manager.getSubTasks();
        assertTrue(subtasksFromManager.isEmpty());
    }
}
