package task;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubTaskTest {

    @Test
    void shouldBeEqualWhenIdSubTaskEqual() {
        EpicTask epic = new EpicTask("epic1", "newEpic");
        Task subTask1 = new SubTask(1,"Uborka", "Uborka v dome", epic.getId(), LocalDateTime.now(),50);
        Task subTask2 = new SubTask(1,"Uborka2", "Uborka v dome2", epic.getId(), LocalDateTime.now().plusDays(1),30);
        assertEquals(subTask1, subTask2, "Программа работает некорректно");
    }
}
