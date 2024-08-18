import allInOne.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import allInOne.InMemoryTaskManager;
import status.Status;

public class Main {
    public static InMemoryTaskManager inMemoryTaskManager = Managers.getDefault();
    public static void main(String[] args) {

        // Создайте две задачи
        System.out.println("2 tasl");
        inMemoryTaskManager.createTask(new Task("Zad 1", "Zad - 1", Status.NEW));
        inMemoryTaskManager.createTask(new Task("zad 2", "Zhpa 2", Status.NEW));

        // Создайте эпик с двумя подзадачами
        inMemoryTaskManager.createEpic(new Epic("epic sk 4", "ny da     mmmmm"));
        inMemoryTaskManager.createSubtask(new Subtask("Put your name ", "subT 1 ep", Status.NEW, 1));
        inMemoryTaskManager.createSubtask(new Subtask("Name 2 ", "Subt 2", Status.NEW, 1));
        System.out.println("  ");

        // Создайте эпик с одной подзадачей
        inMemoryTaskManager.createEpic(new Epic("model.Epic", "Epiocc createsd"));
        inMemoryTaskManager.createSubtask(new Subtask("SubToE[ic", "SubtSk", Status.NEW, 2));
        System.out.println("  ");

        // Распечатайте списки эпиков
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(" ");

        // Измените статусы созданных объектов, распечатайте их.
        inMemoryTaskManager.getTaskById(1).setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.getTaskById(2).setStatus(Status.DONE);

        inMemoryTaskManager.getSubtaskById(1).setStatus(Status.IN_PROGRESS);
        Epic epic1 = inMemoryTaskManager.getEpicById(1);
        epic1.setStatus(inMemoryTaskManager.calculateStatus(epic1));
        System.out.println(epic1);

        inMemoryTaskManager.getSubtaskById(3).setStatus(Status.DONE);
        Epic epic2 = inMemoryTaskManager.getEpicById(2);
        epic2.setStatus(inMemoryTaskManager.calculateStatus(epic2));

        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        System.out.println(inMemoryTaskManager.getAllEpics());

        // Обновление таски по id
        System.out.println("\n---");
        inMemoryTaskManager.createTask(new Task("for change", "descr", Status.NEW));
        System.out.println(inMemoryTaskManager.getAllTasks());
        Task taskNew = new Task("New new", "new descr", Status.DONE, 3);
        inMemoryTaskManager.updateTask(taskNew);
        System.out.println(inMemoryTaskManager.getAllTasks());

        // обновим эпики оп id
        System.out.println("\n---");
        System.out.println(inMemoryTaskManager.getAllEpics());
        Epic epicForUpp = new Epic("newnew", "updated", 1);
        inMemoryTaskManager.updateEpic(epicForUpp);
        System.out.println(inMemoryTaskManager.getAllEpics());

        // обновим сабы по id
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        Subtask subForUp = new Subtask("New Sub ", "Updatetion", Status.NEW, 1);
        System.out.println("---");
        subForUp.setId(2);
        inMemoryTaskManager.updateSubtask(subForUp);
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(inMemoryTaskManager.getAllSubtasks());

        printAllTasks();

        // просмотр задач и заполнение стори
        System.out.println("-----");
        System.out.println("просмотр задач и заполнение стори\n");
        inMemoryTaskManager.getEpicById(1);
        inMemoryTaskManager.getEpicById(1);
        inMemoryTaskManager.getEpicById(2);
        inMemoryTaskManager.getSubtaskById(1);
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getEpicById(1);
        inMemoryTaskManager.getEpicById(1);
        inMemoryTaskManager.getEpicById(2);
        inMemoryTaskManager.getSubtaskById(1);
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getTaskById(1);
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
}

