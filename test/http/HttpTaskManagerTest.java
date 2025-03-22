package http;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.TaskValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerTest() throws IOException {
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
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", LocalDateTime.now(), 5);
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
    public void testAddSubTask() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Epic", "Description");
        manager.addNewEpic(epicTask);
        SubTask task = new SubTask("Test 2", "Testing subtask 2", epicTask.getId(), LocalDateTime.now(), 5);
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<SubTask> SubtasksFromManager = manager.getSubTasks();

        assertNotNull(SubtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, SubtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", SubtasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void addEpicTest() throws IOException, InterruptedException {
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
    void deleteTaskTest() throws TaskValidationException, IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", LocalDateTime.now(), 5);
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
    void getPrioritizedTest() throws TaskValidationException, IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", LocalDateTime.now(), 5);
        Task task2 = new Task("Test 2", "Testing task 2", task.getStartTime().plusDays(1), 5);
        manager.addNewTask(task);
        manager.addNewTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> proritized = manager.getPrioritizedTasks();
        String prioritizedJson = gson.toJson(proritized);
        assertEquals(200, response.statusCode());
        assertEquals(response.body(), prioritizedJson);
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

    @Test
    void getTasksTest() throws TaskValidationException, IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", LocalDateTime.now(), 5);
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
    void getSubtasksOfEpic() throws TaskValidationException, IOException, InterruptedException {
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
}
