package Tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    @Test
    void shouldBeEqualWhenIdTaskEqual() {
        Task task1 = new Task(1,"Uborka", "Uborka v dome");
        Task task2 = new Task(1,"Uborka2", "Uborka v dome2");
        assertEquals(task1, task2, "Программа работает некорректно");
    }
}
