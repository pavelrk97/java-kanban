package managers;

import enums.TaskType;
import model.Epic;
import model.Subtask;
import model.Task;
import status.Status;

import java.io.*;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Integer key : tasks.keySet()) {
                writer.write(tasks.get(key).toString() + "\n");
            }
            for (Integer key : epics.keySet()) {
                writer.write(epics.get(key).toString() + "\n");
            }
            for (Integer key : subtasks.keySet()) {
                writer.write(subtasks.get(key).toString() + "\n");
            }
        } catch (IOException exp) {
            throw new ExceptionsSaveManager("Произошла ошибка записи в файл", exp);
        }
    }

    protected static Task parseFromString(String value) {
        Task parsedTask;
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        TaskType taskType = TaskType.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        switch (taskType) {
            case TASK:
                parsedTask = new Task(id, name, description, status);
                break;

            case EPIC:
                parsedTask = new Epic(id, name, description, status);
                break;

            case SUBTASK:
                int epicId = Integer.parseInt(parts[5]);
                parsedTask = new Subtask(id, name, description, epicId);
                parsedTask.setStatus(status);
                break;
            default:
                throw new ExceptionsSaveManager("Неизвестный тип задачи: " + taskType);
        }
        return parsedTask;
    }

    public static FileBackedTaskManager loadFromFile(File file) {

        HistoryManager historyManager = new InMemoryHistoryManager();
        FileBackedTaskManager manager = new FileBackedTaskManager(historyManager, file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.isEmpty()) {
                    break;
                }
                if (line.contains("id")) {
                    continue;
                }
                Task loadedTask = parseFromString(line);
                int id = loadedTask.getId();
                switch (loadedTask.getType()) {
                    case TASK:
                        manager.tasks.put(id, loadedTask);
                        break;
                    case EPIC:
                        manager.epics.put(id, (Epic) loadedTask);
                        break;
                    case SUBTASK:
                        manager.subtasks.put(id, (Subtask) loadedTask);
                        Epic epic = manager.epics.get(manager.subtasks.get(id).getEpicId());
                        manager.epics.put(id, epic);
                        break;
                }
            }
        } catch (FileNotFoundException exp) {
            throw new RuntimeException("Файл не найден", exp);
        } catch (IOException exp) {
            throw new ExceptionsSaveManager("Произошла ошибка чтения из файла", exp);
        }
        return manager;
    }


    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public Task getTaskById(int id) {
        Task foundTask = super.getTaskById(id);
        if (foundTask != null) {
            save();
            return foundTask;
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        Epic foundEpic = super.getEpicById(id);
        if (foundEpic != null) {
            save();
            return foundEpic;
        } else {
            return null;
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask foundSubtask = super.getSubtaskById(id);
        if (foundSubtask != null) {
            save();
            return foundSubtask;
        } else {
            return null;
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }
}
