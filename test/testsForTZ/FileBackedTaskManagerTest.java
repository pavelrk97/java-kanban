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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

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
    @Test
    public void loadFromNonEmptyFile() {
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

        // Проверяем, что загрузка данных корректна
        loadManager = FileBackedTaskManager.loadFromFile(data);
        assertEquals(saveManager.getAllTasks(), loadManager.getAllTasks(), "Задачи не совпадают после загрузки");
        assertEquals(saveManager.getAllEpics(), loadManager.getAllEpics(), "Эпики не совпадают после загрузки");
        assertEquals(saveManager.getAllSubtasks(), loadManager.getAllSubtasks(), "Подзадачи не совпадают после загрузки");
    }

    @Test
    public void loadFromCreatedFile() throws IOException {
        // Заполняем файл напрямую через Writer
        try (Writer writer = new FileWriter(data)) {
            writer.write("id,type,name,status,description,epic\n");
            writer.write("1,TASK,Task 1,NEW,Description 1,\n");
            writer.write("2,TASK,Task 2,IN_PROGRESS,Description 2,\n");
            writer.write("3,EPIC,Epic 1,NEW,Description Epic 1,\n");
            writer.write("4,SUBTASK,Subtask 1,NEW,Description Subtask 1,3\n");
        }

        // Загружаем данные из файла с помощью лоадера
        loadManager = FileBackedTaskManager.loadFromFile(data);

        // Проверяем, что задачи корректно загружены
        assertEquals(2, loadManager.getAllTasks().size(), "Количество загруженных задач не совпадает");
        assertEquals(1, loadManager.getAllEpics().size(), "Количество загруженных эпиков не совпадает");
        assertEquals(1, loadManager.getAllSubtasks().size(), "Количество загруженных подзадач не совпадает");

        // Проверяем корректность загруженных данных
        Task task1 = loadManager.getAllTasks().get(0);
        Task task2 = loadManager.getAllTasks().get(1);
        Epic epic1 = loadManager.getAllEpics().get(0);
        Subtask subtask1 = loadManager.getAllSubtasks().get(0);

        assertEquals("Task 1", task1.getName());
        assertEquals("Task 2", task2.getName());
        assertEquals("Epic 1", epic1.getName());
        assertEquals("Subtask 1", subtask1.getName());
        assertEquals(epic1.getId(), subtask1.getEpicId(), "Связь подзадачи с эпиком нарушена");
    }
}
