import managers.FileBackedTaskManager;
import managers.InMemoryHistoryManager;
import managers.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import managers.InMemoryTaskManager;
import status.Status;

import java.io.File;
import java.io.IOException;

import static status.Status.DONE;
import static status.Status.NEW;

public class Main {
    static InMemoryHistoryManager historyManager;
    static File data;
    static FileBackedTaskManager saveManager;
    static FileBackedTaskManager loadManager;

    public static void main(String[] args) throws IOException {
        historyManager = Managers.getDefaultHistory();
        data = new File("data.csv");
        // data = File.createTempFile("tasks", "csv");
        saveManager = new FileBackedTaskManager(historyManager, data);


        saveManager.createTask(new Task("Задача 1", "Описание задачи 1", NEW));
        saveManager.createTask(new Task("Задача 2", "Описание задачи 2", NEW));
        saveManager.createEpic(new Epic(3,"Эпик 1", "Описание эпика 1", NEW));
        saveManager.createEpic(new Epic(4,"Эпик 1", "Описание эпика 1", NEW));
        saveManager.createSubtask(new Subtask(5,"Подзадача 1", "Описание подзадачи 1", NEW,3));
        saveManager.createSubtask(new Subtask(6,"Подзадача 1", "Описание подзадачи 1", DONE,4));
        saveManager.createSubtask(new Subtask(7,"Подзадача 1", "Описание подзадачи 1", NEW,3));

        saveManager.deleteAllTasks();

        loadManager = FileBackedTaskManager.loadFromFile(data);

        System.out.println(saveManager.getAllTasks());
        System.out.println(saveManager.getAllEpics());
        System.out.println(saveManager.getAllSubtasks());
        System.out.println("2222");
        System.out.println(loadManager.getAllSubtasks());
        System.out.println(loadManager.getAllTasks());
        }
    }




