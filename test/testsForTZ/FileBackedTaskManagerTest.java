package testsForTZ;

import managers.FileBackedTaskManager;
import managers.InMemoryHistoryManager;
import managers.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static status.Status.*;

public class FileBackedTaskManagerTest extends TaskManagerTest {

    static InMemoryHistoryManager historyManager;
    static File data;
    static FileBackedTaskManager saveManager;
    static FileBackedTaskManager loadManager;

    // Для тестирования методов использовать функцию создания временных файлов File.createTempFile(…)
    @BeforeEach
    void beforeEach() throws IOException {
        historyManager = Managers.getDefaultHistory();
        data = File.createTempFile("tasks", "csv");
        saveManager = new FileBackedTaskManager(historyManager, data);
    }

    // Проверить сохранение и загрузку пустого файла
    @Override
    @Test
    public void saveAndLoadFromEmptyFile() {
        saveManager.save();
        loadManager = FileBackedTaskManager.loadFromFile(data);

        assertTrue(loadManager.getAllTasks().isEmpty());
        assertTrue(loadManager.getAllEpics().isEmpty());
        assertTrue(loadManager.getAllSubtasks().isEmpty());
    }

    // Проверить сохранение нескольких задач, загрузку нескольких задач
    @Override
    @Test
    public void saveAndLoadFromNonEmptyFile() {
        saveManager.createTask(new Task(1,"Задача 1", "Описание задачи 1", NEW, Instant.parse("2024-11-30T10:00:00Z"), Duration.ofMinutes(120)));
        saveManager.createTask(new Task(2,"Задача 2", "Описание задачи 2", NEW, Instant.parse("2024-11-30T10:00:00Z"), Duration.ofMinutes(120)));
        saveManager.createEpic(new Epic(3,"Эпик 1", "Описание эпика 1", NEW));
        saveManager.createSubtask(new Subtask(4,"Подзадача 1", "Описание подзадачи 1", NEW,3));

        System.out.println(saveManager.getAllSubtasks());
        System.out.println(saveManager.getAllEpics());
        System.out.println(saveManager.getAllTasks());
        System.out.println("----------------");
        System.out.println(loadManager.getAllSubtasks());
        System.out.println(loadManager.getAllEpics());
        System.out.println(loadManager.getAllTasks());

        loadManager = FileBackedTaskManager.loadFromFile(data);
        assertEquals(saveManager.getAllTasks(), loadManager.getAllTasks());
        assertEquals(saveManager.getAllEpics(), loadManager.getAllEpics());
        assertEquals(saveManager.getAllSubtasks(), loadManager.getAllSubtasks());
    }

    // Проверка файлового менеджера, созданного из непустого файла
    @Override
    @Test
    public void loadFromNonEmptyFile() {
        // Создаём несколько задач, эпиков и подзадач
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", NEW, Instant.parse("2024-11-30T10:00:00Z"), Duration.ofMinutes(120));
        Task task2 = new Task(2, "Задача 2", "Описание задачи 2", NEW, Instant.parse("2024-11-30T10:00:00Z"), Duration.ofMinutes(120));
        Epic epic1 = new Epic(3, "Эпик 1", "Описание эпика 1", NEW);
        Subtask subtask1 = new Subtask(4, "Подзадача 1", "Описание подзадачи 1", NEW, 3);

        // Добавляем их в менеджер
        saveManager.createTask(task1);
        saveManager.createTask(task2);
        saveManager.createEpic(epic1);
        saveManager.createSubtask(subtask1);

        // Проверяем, что загрузка данных корректна
        loadManager = FileBackedTaskManager.loadFromFile(data);
        assertEquals(saveManager.getAllTasks(), loadManager.getAllTasks(), "Задачи не совпадают после загрузки");
        assertEquals(saveManager.getAllEpics(), loadManager.getAllEpics(), "Эпики не совпадают после загрузки");
        assertEquals(saveManager.getAllSubtasks(), loadManager.getAllSubtasks(), "Подзадачи не совпадают после загрузки");
    }

    @Override
    @Test
    public void loadFromCreatedFile() throws IOException {
        // заполняем файл
        Epic epicin = new Epic("Epa", "do it", NEW, Instant.parse("2024-11-30T10:00:00Z"));
        saveManager.createEpic(epicin);
        Subtask subtaskIn = new Subtask("Sub", "new sub", NEW, 1, Instant.parse("2024-11-30T10:00:00Z"), Duration.ofMinutes(120));
        saveManager.createSubtask(subtaskIn);

        // Загружаем данные из файла с помощью лоадера
        loadManager = FileBackedTaskManager.loadFromFile(data);

        // Проверяем, что задачи корректно загружены
        assertEquals(1, loadManager.getAllEpics().size(), "Количество загруженных эпиков не совпадает");
        assertEquals(1, loadManager.getAllSubtasks().size(), "Количество загруженных подзадач не совпадает");

        // Проверяем корректность загруженных данных
        Epic epic1 = loadManager.getAllEpics().get(0);
        Subtask subtask1 = loadManager.getAllSubtasks().get(0);

        assertEquals("Epa", epic1.getName());
        assertEquals("Sub", subtask1.getName());
        assertEquals(epic1.getId(), subtask1.getEpicId(), "Связь подзадачи с эпиком нарушена");
    }
}
