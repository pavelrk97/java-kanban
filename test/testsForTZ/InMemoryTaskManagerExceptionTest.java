package testsForTZ;

import managers.InMemoryTaskManager;
import managers.Managers;
import model.Epic;
import model.Task;
import org.junit.jupiter.api.Test;
import status.Status;
import java.util.NoSuchElementException;


import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerExceptionTest {

    private final InMemoryTaskManager taskManager = new InMemoryTaskManager(Managers.getDefaultHistory());

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentTask() {
        Task task = new Task("Test Task", "Description", null, null, null);
        task.setId(999);

        assertThrows(NoSuchElementException.class, () -> {
            taskManager.updateTask(task);
        }, "Попытка обновить задачу с некорректным ID должна вызвать исключение");
    }

    @Test
    void shouldNotThrowExceptionWhenCreatingValidTask() {
        Task task = new Task("Valid Task", "Description", Status.NEW, null, null);

        assertDoesNotThrow(() -> taskManager.createTask(task),
                "Создание валидной задачи не должно приводить к исключению");
    }

    @Test
    void shouldHandleNullTaskWhenCreating() {
        assertDoesNotThrow(() -> taskManager.createTask(null),
                "Передача null в метод создания задачи не должна вызывать исключение");
    }

    @Test
    void shouldNotThrowExceptionWhenUpdatingEpicWithoutSubtasks() {
        Epic epic = new Epic("Epic without subtasks", "Description");

        taskManager.createEpic(epic);

        assertDoesNotThrow(() -> {
            epic.setName("Updated Name");
            taskManager.updateEpic(epic);
        }, "Обновление эпика без подзадач не должно вызывать исключение");
    }

    @Test
    void shouldNotThrowExceptionWhenHistoryIsEmpty() {
        assertDoesNotThrow(() -> {
            taskManager.getHistory();
        }, "Получение истории задач с пустым менеджером не должно вызывать исключение");
    }

    @Test
    void shouldThrowExceptionWhenCheckingIntersectionForNullTask() {
        assertThrows(NullPointerException.class, () -> {
            taskManager.checkIntersectionTasks(null);
        }, "Проверка пересечений для null-задачи должна вызывать исключение");
    }

    @Test
    void shouldHandleEmptyDataWhenCalculatingEpicStatus() {
        Epic epic = new Epic("Empty Epic", "Description");
        taskManager.createEpic(epic);

        assertDoesNotThrow(() -> {
            Status status = epic.getStatus();
            assertEquals(Status.NEW, status, "Статус нового эпика без подзадач должен быть NEW");
        }, "Подсчёт статуса эпика без подзадач не должен вызывать исключение");
    }
}
