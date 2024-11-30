package testsForTZ;

import managers.*;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;

import java.time.Duration;
import java.time.Instant;

public class HistoryManagerTest {
    HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void shouldAddTaskToList() {
        Task task = new Task(1, "sdasdas", "descriptor", Status.NEW,
                Instant.parse("2024-11-30T12:00:00Z"), Duration.ofMinutes(20));
        historyManager.add(task);

        Assertions.assertEquals(task, historyManager.getHistory().get(0)); // проверен метод getHistory и addT
    }

    @Test
    void shouldRemoveHistory() {
        Task task = new Task(1, "sdasdas", "descriptor", Status.NEW,
                Instant.parse("2024-11-30T12:00:00Z"), Duration.ofMinutes(20));
        historyManager.add(task);
        historyManager.remove(1);

        int size = 0;
        Assertions.assertEquals(size, historyManager.getHistory().size());
    }

    @Test
    void shouldNotRepeatElementInHistory() {
        Task task = new Task(1, "sdasdas", "descriptor", Status.NEW,
                Instant.parse("2024-11-30T12:00:00Z"), Duration.ofMinutes(20));
        historyManager.add(task);
        historyManager.add(task);

        int size = 1;
        Assertions.assertEquals(size, historyManager.getHistory().size());
    }
}
