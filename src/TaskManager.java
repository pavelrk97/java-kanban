import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    public final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    public final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();

    public void addTask(String name, String description, int status) {
        Task task = new Task(name, description, status);
        task.setStatus(Status.NEW);
        taskHashMap.put(task.getId(), task);
        System.out.println("выполнено");
    }

    public void updateTask(Integer id, String name, String description, int status) {
        if (taskHashMap.containsKey(id)) {
            Task task = new Task(name, description, status);
            taskHashMap.remove(id);
            taskHashMap.put(task.getId(), task);
            System.out.println("выполнено");
        } else {
            System.out.println("Нет задачи по данному id");
        }
    }

    public void addEpic(String name, String description, int status) {
        Epic epic = new Epic(name, description, status);
        epicHashMap.put(epic.getId(), epic);
        System.out.println("выполнено");
    }

    public void updateEpic(Integer id, String name, String description, int status) {
        if (epicHashMap.containsKey(id)) {
            Epic epic = new Epic(name, description, status);
            epicHashMap.remove(id);
            epicHashMap.put(epic.getId(), epic);
            System.out.println("выполнено");
        }
    }

    public void addSubtaskToEpic(Epic epic, String name, String description, int status) {
        int epicId = epic.getId();
        Subtask subtask = new Subtask(name, description, status);
        epicHashMap.get(epicId).epicSubTasks.put(subtask.getId(), subtask);    // обращаемся к нужному эпику по айди,
        // даллее обращаемся к хеш мепу эпика,
        // в котором лежат подзадачи и вносим
        // туда подзадачу, можно добавить
        // проверку на наличие задач
        checkEpicStatus(epic);
    }

    public void updateSubtaskToEpic(Epic epic, int subtaskId , String name, String description, int status) {
        int epicId = epic.getId();

        // проверим условия наличия подзадачи в хешмепе эпика
        if (epicHashMap.containsKey(epicId) && epicHashMap.get(epicId).epicSubTasks.containsKey(subtaskId)) {
            Subtask newSubtask = new Subtask(name, description, status);
            epicHashMap.get(epicId).epicSubTasks.remove(subtaskId);
            epicHashMap.get(epicId).epicSubTasks.put(newSubtask.getId(), newSubtask);
            System.out.println("выполнено");
        } else {
            System.out.println("Нет данного подзадачи с id " + subtaskId + " в эпике с id " + epicId);
        }
        checkEpicStatus(epic);
    }

    public void checkEpicStatus(Epic epic) {
        ArrayList<Status> statuses = new ArrayList<>();
        for (Subtask subtask : epic.epicSubTasks.values()) {
            statuses.add(subtask.getStatus());
        }
        if (statuses.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Status status : statuses) {
            if (status != Status.NEW) {
                allNew = false;
            }
            if (status != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public void printAllTasks() {
        System.out.println(taskHashMap);
    }

    public void printAllEpics() {
        System.out.println(epicHashMap);
    }

    public void printSubtasksInEpic(Epic epic) {
        int epicSize = epic.epicSubTasks.size();
        if (epicSize != 0) {
            for (Subtask subtask : epic.epicSubTasks.values()) {
                System.out.println(subtask);
            }
        }
    }

    public void deleteAllTasks() {
        taskHashMap.clear();
        epicHashMap.clear();
    }

    public void printById(int id) {
        if (taskHashMap.containsKey(id)) {
            System.out.println(taskHashMap.get(id));
        } else if (epicHashMap.containsKey(id)) {
            System.out.println(epicHashMap.get(id));
        } else {
            for (Epic epic : epicHashMap.values()) {
                if (epic.epicSubTasks.containsKey(id)) {
                    System.out.println(epic.epicSubTasks.get(id));
                }
            }
        }
    }

    public void deleteById(int id) {
        if (taskHashMap.containsKey(id)) {
            taskHashMap.remove(id);
        } else if (epicHashMap.containsKey(id)) {
            epicHashMap.remove(id);
        } else {
            for (Epic epic : epicHashMap.values()) {
                if (epic.epicSubTasks.containsKey(id)) {
                    epic.epicSubTasks.remove(id);
                }
            }
        }
    }
}






