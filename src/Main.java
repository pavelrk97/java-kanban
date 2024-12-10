import managers.FileBackedTaskManager;
import managers.InMemoryHistoryManager;
import managers.Managers;
import model.Epic;
import model.Subtask;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import static status.Status.DONE;
import static status.Status.NEW;

public class Main {
    static InMemoryHistoryManager historyManager;
    static File data;
    static FileBackedTaskManager saveManager;

    public static void main(String[] args) throws IOException {
        historyManager = Managers.getDefaultHistory();
        data = new File("data.csv");
        // data = File.createTempFile("tasks", "csv");
        saveManager = new FileBackedTaskManager(historyManager, data);
        System.out.println("epic creating");

        saveManager.createEpic(new Epic("Epic very", "to do list", NEW, Instant.now()));
        saveManager.createSubtask(new Subtask("NamuSub2", "Descriptor sub1", DONE, 1, Instant.now().plus(Duration.ofMinutes(20)), Duration.ofMinutes(150)));

        System.out.println(saveManager.getAllEpics());
        System.out.println(saveManager.getAllSubtasks());
        Subtask ssss = saveManager.getSubtaskById(2);
        System.out.println(ssss.getEndTime());
        Epic epic = saveManager.getEpicById(1);
        System.out.println(epic.getEndTime());
    }
}




