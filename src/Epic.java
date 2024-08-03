import java.util.HashMap;

public class Epic extends Task{
    HashMap<Integer, SubTask> epicSubTasks;

    public Epic(String name, String description) {
        super(name, description);
        this.epicSubTasks = new HashMap<>();
        TaskManager taskManager = new TaskManager();
    }


    public void addSubTask(SubTask subTask) {
        epicSubTasks.put(subTask.getId(), subTask);
        System.out.println("Подзадача внесена " + subTask);
    }

    public void updateSubtask(int oldId, SubTask subTask) {
        epicSubTasks.put(oldId, subTask);
        System.out.println("Подзадача обновлена " + epicSubTasks);
        subTask.setId(oldId);
    }

    public void deleteAllSubTasks() {
        epicSubTasks.clear();
    }

    public void deleteIdSubtask(int id) {
        epicSubTasks.remove(id);
    }


}
