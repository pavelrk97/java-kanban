package testsForTZ;

import managers.*;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;

import java.time.Duration;
import java.time.Instant;

/* Для расчёта статуса Epic. Граничные условия:
a. Все подзадачи со статусом NEW.
b. Все подзадачи со статусом DONE.
c. Подзадачи со статусами NEW и DONE.
d. Подзадачи со статусом IN_PROGRESS.*/

public class EpicTest {
    private TaskManager taskManager;

    @BeforeEach
    void init() {
        taskManager = Managers.getDefault();
    }

    @Test
    void epicStatusNewAll() {
        // prepare
        Epic epicIdeal = new Epic("Epic 0", "Epic 0 des", Status.NEW, Instant.now());
        taskManager.createEpic(new Epic("Epic 0", "Epic 0 des", Status.NEW, Instant.now()));
        Subtask subtask1 = new Subtask("Name", "Desct1", Status.NEW, 1,
                Instant.parse("2024-11-30T12:00:00Z"), Duration.ofMinutes(100));
        Subtask subtask2 = new Subtask("Name2", "Desct2", Status.NEW, 1,
                Instant.parse("2024-11-30T12:00:00Z").plus(Duration.ofMinutes(200)), Duration.ofMinutes(100));

        // do
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        // check
        Epic epic = taskManager.getEpicById(1);
        Assertions.assertEquals(epic.getStatus(), epicIdeal.getStatus());
    }

    @Test
    void epicStatusDoneAll() {
        // prepare
        Epic epicIdeal = new Epic("Epic 0", "Epic 0 des", Status.DONE, Instant.now());
        taskManager.createEpic(new Epic("Epic 0", "Epic 0 des", Status.NEW, Instant.now()));
        Subtask subtask1 = new Subtask("Name", "Desct1", Status.DONE, 1,
                Instant.parse("2024-11-30T12:00:00Z"), Duration.ofMinutes(100));
        Subtask subtask2 = new Subtask("Name2", "Desct2", Status.DONE, 1,
                Instant.parse("2024-11-30T12:00:00Z").plus(Duration.ofMinutes(200)), Duration.ofMinutes(100));

        // do
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        // check
        Epic epic = taskManager.getEpicById(1);
        Assertions.assertEquals(epic.getStatus(), epicIdeal.getStatus());
    }

    @Test
    void epicStatusNewAndDoneAll() {
        // prepare
        Epic epicIdeal = new Epic("Epic 0", "Epic 0 des", Status.IN_PROGRESS, Instant.now());
        taskManager.createEpic(new Epic("Epic 0", "Epic 0 des", Status.NEW, Instant.now()));
        Subtask subtask1 = new Subtask("Name", "Desct1", Status.NEW, 1,
                Instant.parse("2024-11-30T12:00:00Z"), Duration.ofMinutes(100));
        Subtask subtask2 = new Subtask("Name2", "Desct2", Status.DONE, 1,
                Instant.parse("2024-11-30T12:00:00Z").plus(Duration.ofMinutes(200)), Duration.ofMinutes(100));

        // do
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        // check
        Epic epic = taskManager.getEpicById(1);
        Assertions.assertEquals(epic.getStatus(), epicIdeal.getStatus());
    }

    @Test
    void epicStatusProgressAll() {
        // prepare
        Epic epicIdeal = new Epic("Epic 0", "Epic 0 des", Status.IN_PROGRESS, Instant.now());
        taskManager.createEpic(new Epic("Epic 0", "Epic 0 des", Status.NEW, Instant.now()));
        Subtask subtask1 = new Subtask("Name", "Desct1", Status.IN_PROGRESS, 1,
                Instant.parse("2024-11-30T12:00:00Z"), Duration.ofMinutes(100));
        Subtask subtask2 = new Subtask("Name2", "Desct2", Status.IN_PROGRESS, 1,
                Instant.parse("2024-11-30T12:00:00Z").plus(Duration.ofMinutes(200)), Duration.ofMinutes(100));

        // do
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        // check
        Epic epic = taskManager.getEpicById(1);
        Assertions.assertEquals(epic.getStatus(), epicIdeal.getStatus());
    }
}
