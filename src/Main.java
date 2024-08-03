public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        System.out.println("  ");
        System.out.println("2 tasks");
        Task task1 = new Task("Do this", "fast");
        Task task2 = new Task("Do this", "slow");
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        System.out.println(" ");
        System.out.println("1 epic + 2 subT");
        Epic epic1 = new Epic("Epic1", "descript");
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("SubT 1 ", "adsds");
        SubTask subTask2 = new SubTask("Subt 2", " subr br br");
        taskManager.addSubTaskToEpic(epic1, subTask1);
        taskManager.addSubTaskToEpic(epic1, subTask2);

        System.out.println("  ");
        System.out.println("PrintAll");
        System.out.println("Tasks are: " + taskManager.fullListTasks());
        System.out.println("Epics here: " + taskManager.fullListEpics());
        System.out.println("SubT for epic1 are: " + taskManager.fullListSubTasks(epic1));

        System.out.println("  ");
        System.out.println("Status swing");
        System.out.println(epic1);
        System.out.println("Создаем новую подзадачу со статусом в progress и заменяем ей старую");
        SubTask subTaskSwing = new SubTask("Changed", "Status for Epic", Status.IN_PROGRESS);
        taskManager.updateSubTaskToEpic(epic1, subTask2, subTaskSwing);
        System.out.println(epic1);

        System.out.println(" ");
        System.out.println("Delete subtusk + status check");
        System.out.println(taskManager.getEpicHashMap());
        System.out.println(taskManager.fullListSubTasks(epic1));
        taskManager.deleteById(-1615900613);
        System.out.println(taskManager.getEpicHashMap());
    }
}
