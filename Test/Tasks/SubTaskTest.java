package Tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubTaskTest {

    @Test
    void shouldBeEqualWhenIdSubTaskEqual() {
        EpicTask epic = new EpicTask("epic1", "newEpic");
        Task subTask1 = new SubTask(1,"Uborka", "Uborka v dome", epic.getId());
        Task subTask2 = new SubTask(1,"Uborka2", "Uborka v dome2", epic.getId());
        assertEquals(subTask1, subTask2, "Программа работает некорректно");

    }
}
