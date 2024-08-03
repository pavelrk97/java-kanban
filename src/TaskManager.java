import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, Epic> subTaskHashMap = new HashMap<>();

    public HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    public HashMap<Integer, Epic> getEpicHashMap() {
        return epicHashMap;
    }

    public void addTask(Task task) {
        taskHashMap.put(task.getId(), task);
        System.out.println("Задача сохранена " + task);
    }

    // если вносить старый id и новый таск, то из-за большого значения id неудобно вписывать. поэтому как 2 объекта
    // важно не забывать присваивать старый id новым объектам, которыми заменяем старое, во всех методах
    public void updateTask(Task taskOld, Task task) {
        if (taskHashMap.containsKey(taskOld.getId())) {
            taskHashMap.put(taskOld.getId(), task);
            System.out.println("обновлена задача " + task);
            task.setId(taskOld.getId());   // обновление id на id предшественника
        } else {
            System.out.println("Нет задачи по id " + taskOld.getId());
        }
    }

    public void addEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
        System.out.println("добавлена задача epic " + epic);
    }

    public void updateEpic(Epic oldEpic, Epic epic) {
        if (epicHashMap.containsKey(oldEpic.getId())) {
            epicHashMap.put(oldEpic.getId(), epic);
            System.out.println("обновалена задача epic " + epic);
            epic.setId(oldEpic.getId());
        } else {
            System.out.println("нет epic c id " + epic.getId());
        }
    }

    public void addSubTaskToEpic(Epic epic, SubTask subTask) {
        int epicId = epic.getId();
        epic.addSubTask(subTask);
        updateEpicStatus(epic);
    }

    public void updateSubTaskToEpic(Epic epic, SubTask oldSubTask,SubTask subTask) {
        int epicId = epic.getId();
        int oldSubId = oldSubTask.getId();

        // проверим условия наличия подзадачи в хешмепе эпика
        if (epicHashMap.containsKey(epic.getId()) && epicHashMap.get(epic.getId()).epicSubTasks.containsKey(oldSubTask.getId())) {
            epic.updateSubtask(oldSubId, subTask);
        } else {
            System.out.println("Нет данного подзадачи с id " + oldSubTask + " в эпике с id " + epicId);
        }
        subTask.setId(oldSubTask.getId());
        updateEpicStatus(epic);
    }

    public void updateEpicStatus(Epic epic) {
        List<Status> statuses = new ArrayList<>();
        for (SubTask subTask : epic.epicSubTasks.values()) {
            statuses.add(subTask.getStatus());
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

    public ArrayList<Task> fullListTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.addAll(taskHashMap.values());
        tasks.addAll(epicHashMap.values());
        tasks.addAll(subTaskHashMap.values());
        return tasks;
    }

    public ArrayList<Epic> fullListEpics() {
        ArrayList<Epic> epics = new ArrayList<>();
        epics.addAll(epicHashMap.values());
        return epics;
    }

    public ArrayList<SubTask> fullListSubTasks(Epic epic) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        int epicSize = epic.epicSubTasks.size();
        if (epicSize != 0) {
            subTasks.addAll(epic.epicSubTasks.values());
        }
        return subTasks;
    }

    public void deleteAllTasks() {
        taskHashMap.clear();
    }

    public void deleteAllEpics() {
        epicHashMap.clear();
    }

    public void deleteAllSubtasks(Epic epic) {
        epic.epicSubTasks.clear();
    }

    public void deleteById(int id) {
        if (taskHashMap.containsKey(id)) {
            taskHashMap.remove(id);
        } else if (epicHashMap.containsKey(id)) {
            epicHashMap.get(id).deleteAllSubTasks();
            epicHashMap.remove(id);
        } else {
            for (Epic epic : epicHashMap.values()) {
                if (epic.epicSubTasks.containsKey(id)) {
                    epic.epicSubTasks.remove(id);
                    updateEpicStatus(epic);
                } else {
                    System.out.println("no id");
                }
            }
        }
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
}







