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

public class EpicsHandlerTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public EpicsHandlerTest() throws IOException {
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
    void addEpicTest() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask(0, "Epic", "Description", LocalDateTime.now(), 10);
        String epicJson = gson.toJson(epicTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<EpicTask> EpicsFromManager = manager.getEpicTasks();

        assertNotNull(EpicsFromManager, "Задачи не возвращаются");
        assertEquals(1, EpicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Epic", EpicsFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void getSubtasksOfEpic() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Epic", "Description");
        manager.addNewEpic(epicTask);
        SubTask task = new SubTask("Test 2", "Testing subtask 2", epicTask.getId(), LocalDateTime.now(), 5);
        manager.addNewSubTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<SubTask> subFromEpic = epicTask.getSubTasksIds().stream()
                .map(subId -> manager.getSubTask(subId))
                .toList();
        String subFromEpicJson = gson.toJson(subFromEpic);

        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertEquals(subFromEpicJson, response.body(), "Неверное тело ответа");
    }

    @Test
    void getEpicsTest() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Epic", "Description");
        manager.addNewEpic(epicTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .version(HttpClient.Version.HTTP_1_1)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<EpicTask> epics = manager.getEpicTasks();
        String epicsJson = gson.toJson(epics);
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertEquals(epicsJson, response.body(), "Неверное тело ответа");
    }

    @Test
    void getEpicFromId() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Epic", "Description");
        int idForRequest = manager.addNewEpic(epicTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + idForRequest);
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String epicJson = gson.toJson(epicTask);
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertEquals(epicJson, response.body(), "Неверное тело ответа");

    }

    @Test
    void deleteEpicTest() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Epic", "Description");
        int idForRequest = manager.addNewEpic(epicTask);
        assertFalse(manager.getEpicTasks().isEmpty());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + idForRequest);
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertTrue(manager.getEpicTasks().isEmpty());
    }
}
