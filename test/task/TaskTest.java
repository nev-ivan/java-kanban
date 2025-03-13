package task;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    @Test
    void shouldBeEqualWhenIdTaskEqual() {
        Task task1 = new Task(1,"Uborka", "Uborka v dome", LocalDateTime.now().plusDays(1),30);
        Task task2 = new Task(1,"Uborka2", "Uborka v dome2",LocalDateTime.now(),30);
        assertEquals(task1, task2, "Программа работает некорректно");
    }
}
