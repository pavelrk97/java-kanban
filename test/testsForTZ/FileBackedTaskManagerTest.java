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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static status.Status.*;

public class FileBackedTaskManagerTest {

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
    @Test
    public void saveAndLoadFromEmptyFile() {
        saveManager.save();
        loadManager = FileBackedTaskManager.loadFromFile(data);

        assertTrue(loadManager.getAllTasks().isEmpty());
        assertTrue(loadManager.getAllEpics().isEmpty());
        assertTrue(loadManager.getAllSubtasks().isEmpty());
    }

    // Проверить сохранение нескольких задач, загрузку нескольких задач
    @Test
    public void saveAndLoadFromNonEmptyFile() {
        saveManager.createTask(new Task(1,"Задача 1", "Описание задачи 1", NEW));
        saveManager.createTask(new Task(2,"Задача 2", "Описание задачи 2", NEW));
        saveManager.createEpic(new Epic(3,"Эпик 1", "Описание эпика 1", NEW));
        saveManager.createSubtask(new Subtask(4,"Подзадача 1", "Описание подзадачи 1", NEW,3));

        loadManager = FileBackedTaskManager.loadFromFile(data);
        assertEquals(saveManager.getAllTasks(), loadManager.getAllTasks());
        assertEquals(saveManager.getAllEpics(), loadManager.getAllEpics());
        assertEquals(saveManager.getAllSubtasks(), loadManager.getAllSubtasks());
    }

    // Проверка файлового менеджера, созданного из непустого файла
    @Test
    public void LoadFromNonEmptyFile() {
        // Создаём несколько задач, эпиков и подзадач
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", NEW);
        Task task2 = new Task(2, "Задача 2", "Описание задачи 2", NEW);
        Epic epic1 = new Epic(3, "Эпик 1", "Описание эпика 1", NEW);
        Subtask subtask1 = new Subtask(4, "Подзадача 1", "Описание подзадачи 1", NEW, 3);

        // Добавляем их в менеджер
        saveManager.createTask(task1);
        saveManager.createTask(task2);
        saveManager.createEpic(epic1);
        saveManager.createSubtask(subtask1);

        // Сохраняем данные в файл
        saveManager.save();

        // Проверяем, что загрузка данных корректна
        loadManager = FileBackedTaskManager.loadFromFile(data);
        assertEquals(saveManager.getAllTasks(), loadManager.getAllTasks(), "Задачи не совпадают после загрузки");
        assertEquals(saveManager.getAllEpics(), loadManager.getAllEpics(), "Эпики не совпадают после загрузки");
        assertEquals(saveManager.getAllSubtasks(), loadManager.getAllSubtasks(), "Подзадачи не совпадают после загрузки");
    }
}
