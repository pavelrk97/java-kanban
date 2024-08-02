public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        System.out.println("Test 1: 2 tasks are created");
        taskManager.addTask("Task 1", "new task ", 1);
        taskManager.addTask("Task 2 ", "both are added to HashMap rapidly", 1);

        System.out.println("Test 1: Epic is created");
        Epic epic = new Epic("Epic 1", "created by hand, not added automatically to map", 1);
        taskManager.epicHashMap.put(epic.getId(), epic);

        System.out.println("Test 1:Sub in epic are created");
        taskManager.addSubtaskToEpic(epic, "Subtask 1", "also it could be an obj instead of words", 1);
        taskManager.addSubtaskToEpic(epic, "Sub 2", "Push push", 2);

        System.out.println("Test 2: printing all");
        taskManager.printAllTasks();
        taskManager.printAllEpics();
        taskManager.printSubtasksInEpic(epic);

        System.out.println("SubT status changing by id");
        System.out.println(epic);
        taskManager.updateSubtaskToEpic(epic, 5, "Changed to NEW", "Placeholder", 1);
        System.out.println(epic);

        System.out.println("удаление");
        taskManager.printById(5);
        System.out.println(epic);
    }
}
