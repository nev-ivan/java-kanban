package Tasks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTaskTest {

    @Test
    void shouldBeEqualWhenIdEpicTaskEqual() {
        EpicTask epic1 = new EpicTask(1,"epic1", "newEpic");
        EpicTask epic2 = new EpicTask(1,"epic2", "newEpic2");
        assertEquals(epic1, epic2, "Программа работает некорректно");
    }
}