package managers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;
    private final InMemoryTaskManager manager = new InMemoryTaskManager();

    public FileBackedTaskManager(HistoryManager historyManager, String path) {
        this.file = new File(path);
    }

    public void save() {
        try (Writer writer = new FileWriter(file)) {
            List<Task> allTasks = getAllTasks();
            for (Task task : allTasks) {
                String taskAsString = null; //
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}
