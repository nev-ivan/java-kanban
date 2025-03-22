package http;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Task;

import java.time.LocalDateTime;


public class SerializationTest {

    Gson gson = HttpTaskServer.getGson();

    @Test
    void serializationTest() {
        Task task = new Task("Task", "Description", LocalDateTime.now(), 10);
        String jsonStr = gson.toJson(task);
        System.out.println(jsonStr);
        Task task1 = gson.fromJson(jsonStr, Task.class);
        Assertions.assertEquals(task, task1);
    }
}
