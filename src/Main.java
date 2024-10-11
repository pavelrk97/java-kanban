import managers.*;
import model.Epic;
import model.Subtask;
import model.Task;
import status.Status;

import java.io.File;
import java.io.IOException;

public class Main {

    static InMemoryHistoryManager historyManager;
    static File data;
    static FileBackedTaskManager saveManager;
    static FileBackedTaskManager loadManager;

    public static void main(String[] args) {
        // Инициализация менеджера истории
        historyManager = Managers.getDefaultHistory();

        // Создание временного файла для хранения задач
        data = new File("data.csv");

        // Инициализация менеджера задач с сохранением в файл
        saveManager = new FileBackedTaskManager(historyManager, data);

        // Создание задачи
        Task task = new Task(1, "Пример задачи", "Описание задачи", Status.NEW);
        Epic epic = new Epic(1, "Epicname", "weweweew 23423", Status.NEW);
        saveManager.createTask(task);
        saveManager.createEpic(epic);

        System.out.println("Задачи загружены из файла: ");
        System.out.println(saveManager.getAllTasks());
        System.out.println(saveManager.getAllEpics());

        // Загрузка задач из файла
        loadManager = FileBackedTaskManager.loadFromFile(data);

        System.out.println(loadManager.getAllEpics());
        System.out.println(loadManager.getAllTasks());
        // Выводим задачи

    }
}
    /*
    public static InMemoryTaskManager inMemoryTaskManager = Managers.getDefault();

    public static void main(String[] args) {

        // Создайте две задачи
        inMemoryTaskManager.createTask(new Task("Zad 1", "Zad - 1", Status.NEW));
        inMemoryTaskManager.createTask(new Task("zad 2", "Zhpa 12", Status.NEW));

        // Создайте эпик с 3 подзадачами
        inMemoryTaskManager.createEpic(new Epic("epic with 3", "ny da"));
        inMemoryTaskManager.createSubtask(new Subtask("Put your name 1", "subT 1", Status.NEW, 3));
        inMemoryTaskManager.createSubtask(new Subtask("Name 2 ", "Subt 2", Status.NEW, 3));
        inMemoryTaskManager.createSubtask(new Subtask("Name 2 ", "Subt 3", Status.NEW, 3));
        System.out.println("  ");

        // Создайте эпик пустой
        inMemoryTaskManager.createEpic(new Epic("model.Epic", "Epiocc createsd"));

        printAllTasks();
        // запросите задачи
        System.out.println("Запрос задач");
        System.out.println(inMemoryTaskManager.getTaskById(2));
        System.out.println(inMemoryTaskManager.getEpicById(3));
        System.out.println(inMemoryTaskManager.getSubtaskById(5));
        System.out.println(inMemoryTaskManager.getTaskById(2));
        System.out.println(inMemoryTaskManager.getTaskById(1));

        // просмотр задач
        System.out.println("-----");
        System.out.println("просмотр задач и заполнение стори\n");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println(inMemoryTaskManager.getHistory().size());

        // удалить таску по айди и она удалится из стори
        System.out.println("  ");
        System.out.println("Удалить таску по айди и она удалится из стори");
        inMemoryTaskManager.deleteTaskById(2);
        printAllTasks();

        // просмотр задач
        System.out.println("-----");
        System.out.println("просмотр задач и заполнение стори\n");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println(inMemoryTaskManager.getHistory().size());

        // удалить эпик с сабтаксками по айди и она удалится из стори
        System.out.println("Удалить эпик с сабтаксками по айди и они удалятся из стори");
        inMemoryTaskManager.deleteEpicById(3);
        printAllTasks();

        // просмотр задач
        System.out.println("-----");
        System.out.println("просмотр задач и заполнение стори\n");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println(inMemoryTaskManager.getHistory().size());

        // Создайте эпик с 3 подзадачами и удаление сабов
        inMemoryTaskManager.createEpic(new Epic("epic with 3", "ny da"));
        inMemoryTaskManager.createSubtask(new Subtask("Put your name 1", "subT 1", Status.NEW, 7));
        inMemoryTaskManager.createSubtask(new Subtask("Name 2 ", "Subt 2", Status.NEW, 7));
        inMemoryTaskManager.createSubtask(new Subtask("Name 2 ", "Subt 3", Status.NEW, 7));
        System.out.println("  ");
        printAllTasks();
        inMemoryTaskManager.getSubtaskById(9);
        inMemoryTaskManager.getSubtaskById(10);
        System.out.println("просмотр задач\n");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println(inMemoryTaskManager.getHistory().size());
        inMemoryTaskManager.deleteAllEpics();
        printAllTasks();
        System.out.println("просмотр задач\n");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println(inMemoryTaskManager.getHistory().size());
    }

    private static void printAllTasks() {
        System.out.println("Задачи:");
        for (Task task : Main.inMemoryTaskManager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : Main.inMemoryTaskManager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : Main.inMemoryTaskManager.getSubtaskList(epic)) {
                System.out.println("--> " + task);
            }
        }

        System.out.println("Подзадачи:");
        for (Task subtask : Main.inMemoryTaskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }
    }
    */


