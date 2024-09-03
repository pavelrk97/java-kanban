package testsForTZ;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import managers.Managers;
import managers.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void init() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addNewTaskShouldSaveTaskEqualsById() {
        // prepare
        Task task = new Task("Name", "Desct1");
        Task expectedTask = new Task("Name2", "Desct1");
        expectedTask.setId(1);

        // do
        taskManager.createTask(task);

        // check
        Assertions.assertNotNull(taskManager.getTaskById(1));
        Assertions.assertEquals(expectedTask, task);

    }

    @Test
    void updateTaskShouldUpdateTaskWithSpecificatedId() {
        // prepare
        taskManager.createTask(new Task("Task1", "Task des"));
        Task newTask = new Task("Task2", "Updated", Status.DONE, 1);


        // do
        taskManager.updateTask(newTask);

        // check
        Assertions.assertEquals(taskManager.getTaskById(1), newTask);
    }

    @Test
    void epicsAreEqual () {
        // prepare
        Epic epic = new Epic("Epic", "asda");
        Epic epic2 = new Epic("Epic", "asda");

        // do
        epic.setId(1);
        epic2.setId(1);

        // check
        Assertions.assertEquals(epic, epic2);
    }

    @Test
    void subsAreEqual () {
        // prepare
        Subtask subtask1 = new Subtask("asda", "qweq", Status.NEW, 1);
        Subtask subtask2 = new Subtask("asdaweqw", "qweqqweqw", Status.NEW, 2);

        // do
        subtask1.setId(1);
        subtask2.setId(1);

        // check
        Assertions.assertEquals(subtask1, subtask2);
    }

    @Test
    void getDefaultShouldInitializeInMemoryTaskManager() {
        assertInstanceOf(InMemoryTaskManager.class, Managers.getDefault());
    }

    @Test
    void getDefaultHistoryShouldInitializeInMemoryHistoryManager() {
        assertInstanceOf(InMemoryHistoryManager.class, Managers.getDefaultHistory());
    }

    @Test
    void InMemoryTaskManagerShouldAddTaskShouldLocateById () {
        // prepare
        taskManager.createTask(new Task("Vamos", "Faster"));

        Task taskIdeal = new Task("Vamos", "Faster");
        taskIdeal.setId(1);

        // check
        Assertions.assertEquals(taskIdeal, taskManager.getTaskById(1));
    }

    @Test
    void InMemoryTaskManagerShouldAddEpicShouldLocateById () {
        // prepare
        taskManager.createEpic(new Epic("Vamos", "Faster"));

        Epic epicIdeal = new Epic("Vamos", "Faster");
        epicIdeal.setId(1);

        // check
        Assertions.assertEquals(epicIdeal, taskManager.getEpicById(1));
    }

    @Test
    void InMemoryTaskManagerShouldAddSubShouldLocateById () {
        // prepare
        taskManager.createEpic(new Epic("Vamos", "Faster"));
        taskManager.createSubtask(new Subtask("Sub", "for epic",Status.NEW, 1));
        Subtask subtaskIdeal = new Subtask("Sub", "for epic", Status.NEW, 1);
        subtaskIdeal.setId(2);

        // check
        Assertions.assertEquals(subtaskIdeal, taskManager.getSubtaskById(2));
    }

    @Test
    void noConflictWhenCreatedTaskSameID() {  // не нужный тест, тк id дается при добавлении, отличный от предыдущего
        // prepare
        int lengthListShouldBe = 2;
        taskManager.createTask(new Task("First", "First")); // id = 1

        // do
        Task task = new Task("asd", "wqe");
        task.setId(1);
        taskManager.createTask(task);

        // check
        int currentLenght = taskManager.getAllTasks().size();
        Assertions.assertEquals(lengthListShouldBe, currentLenght);
    }

    @Test
    void allInTaskIsSame() {
        // prepare
        Task idealTask = new Task("ideal", "task", Status.DONE);

        // do
        taskManager.createTask(idealTask);

        // check
        Task taskInManager = taskManager.getTaskById(1);
        String nameInside = taskInManager.getName();
        String desInside = taskInManager.getDescription();
        Status statusInside = taskInManager.getStatus();

        String nameIdeal = idealTask.getName();
        String desIdeal = idealTask.getDescription();
        Status statusIdeal = idealTask.getStatus();

        Assertions.assertEquals(nameIdeal, nameInside);
        Assertions.assertEquals(desIdeal, desInside);
        Assertions.assertEquals(statusIdeal, statusInside);
    }


    @Test
    void historyShowsPrevious() {
        // prepare
        // создаем задачи
        Task taskIdeal = new Task("Task", "Idal");
        taskManager.createTask(taskIdeal);

        Task taskIdeal2 = new Task("Task2", "Ideal2");
        taskManager.createTask(taskIdeal2);

        // do
        // просмотр задач
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);

        // check
        Assertions.assertEquals(taskIdeal2, taskManager.getHistory().get(1));
        Assertions.assertEquals(taskIdeal, taskManager.getHistory().get(0));
    }

    @Test
    void subNotAddedToEpicWithSameId() {
        // prepare
        Epic epic = new Epic("Wasd", "Wasd");
        Subtask subtask = new Subtask("for epic", "for", Status.DONE, 1);
        Subtask subChangeEpic = new Subtask("exp", "experiment", Status.NEW, 1);

        // do
        taskManager.createEpic(epic);  // epicId = 1
        taskManager.createSubtask(subtask); // subtaskId = 1, has epicId = 1

        subChangeEpic.setId(1);
        taskManager.createSubtask(subChangeEpic); // сабтаск не стал эпиком

        // check
        String epicName = epic.getName();
        String epicDes = epic.getDescription();

        Epic epicFromList = taskManager.getEpicById(1);
        String epicFromName = epicFromList.getName();
        String epicFromDes = epicFromList.getDescription();

        boolean isEpicNameEqual = epicName.equals(epicFromName);
        boolean isEpicDesEqual = epicDes.equals(epicFromDes);

        if (isEpicNameEqual && isEpicDesEqual) {
            System.out.println("Эпик из списка равен эпику эталонному, наложение не произошло,\n" +
                    "изменился id второго саба и он добавился в эпик, а не стал эпиком");
        }

        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
    }

    @Test
    void addedTaskEqualsHistoryTask() {
        // prepare
        taskManager.createTask(new Task("Zad 1", "Zad - 1", Status.NEW));
        taskManager.createTask(new Task("zad 2", "Zhpa 12", Status.NEW));
        Task idealTask = new Task("Zad 1", "Zad - 1", Status.NEW);
        idealTask.setId(1);
        // do
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        Task checkedTask = taskManager.getHistory().get(0);
                // check
        Assertions.assertEquals(idealTask, checkedTask);
    }

    @Test
    void whenDeletedFromListDeletedFromHistory() {
        // prepare
        int lenIdeal;
        int lenHistory;
        taskManager.createTask(new Task("Zad 1", "Zad - 1", Status.NEW));
        taskManager.createTask(new Task("zad 2", "Zhpa 12", Status.NEW));

        // do
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        lenHistory = taskManager.getHistory().size();
        lenIdeal = taskManager.getAllTasks().size();
        taskManager.deleteTaskById(1);

        // check
        Assertions.assertEquals(lenIdeal, lenHistory);
    }

    @Test
    void whenAddedToListAddedToHistory() {
        // prepare
        int lenIdeal;
        int lenHistory;
        taskManager.createTask(new Task("Zad 1", "Zad - 1", Status.NEW));
        taskManager.createTask(new Task("zad 2", "Zhpa 12", Status.NEW));

        // do
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        lenHistory = taskManager.getHistory().size();
        lenIdeal = taskManager.getAllTasks().size();

        // check
        Assertions.assertEquals(lenIdeal, lenHistory);
    }

    @Test
    void deletedSubtasksHasNoId() {
        // prepare
        taskManager.createEpic(new Epic("epic", "sdsd"));
        taskManager.createSubtask(new Subtask("new sub1", "sub1", Status.NEW, 1));
        taskManager.createSubtask(new Subtask("new sub2", "sub2", Status.NEW, 1));

        // do
        taskManager.deleteSubtaskById(3);

        // check
        Assertions.assertEquals(null, taskManager.getSubtaskById(3));
    }
}



