package managers;

import enums.TaskType;
import model.Epic;
import model.Subtask;
import model.Task;
import status.Status;

import java.io.*;
import java.time.Duration;
import java.time.Instant;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            // Заголовок файла
            writer.write("id,type,name,status,description,duration,startTime,epic\n");

            // Сохраняем все задачи
            for (Task task : getAllTasks()) {
                writer.write(task.toString() + "\n");
            }

            // Сохраняем все эпики
            for (Epic epic : getAllEpics()) {
                writer.write(epic.toString() + "\n");
            }

            // Сохраняем все подзадачи
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(subtask.toString() + "\n");
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

      if (taskType.equals(TaskType.SUBTASK)) {
          Instant startTime = Instant.parse(parts[6]);
          Long durationInMinutes = Long.valueOf(parts[5]);
          Duration duration = Duration.ofMinutes(durationInMinutes);
          int epicId = Integer.valueOf(parts[7]);
          return new Subtask(id, name, description, status, epicId, startTime, duration);
      } else if (taskType.equals(TaskType.TASK)) {
          Instant startTime = Instant.parse(parts[6]);
          Long durationInMinutes = Long.valueOf(parts[5]);
          Duration duration = Duration.ofMinutes(durationInMinutes);
          return new Task(id, name, description, status, startTime, duration);
      } else {
          return new Epic(id, name, description, status);
      }
    }

    public static FileBackedTaskManager loadFromFile(File file) {

        HistoryManager historyManager = new InMemoryHistoryManager();
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(historyManager, file);

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
                        fileBackedTaskManager.tasks.put(loadedTask.getId(), loadedTask);
                        break;
                    case EPIC:
                        fileBackedTaskManager.epics.put(loadedTask.getId(), (Epic) loadedTask);
                        break;
                    case SUBTASK:
                        fileBackedTaskManager.subtasks.put(loadedTask.getId(), (Subtask) loadedTask);
                        break;
                }
                fileBackedTaskManager.taskId = id + 1; // не даем заменить предыдущие таски при загрузке, уникальность
                // важное
            }

            fileBackedTaskManager.subtasks.values().forEach(subtask -> {
                Epic epic = fileBackedTaskManager.epics.get(subtask.getEpicId());
                epic.addSubtask(subtask.getId());

            });

        } catch (FileNotFoundException exp) {
            throw new  IllegalArgumentException("Файл не найден", exp);  // приведена более узкая шибка
        } catch (IOException exp) {
            throw new ExceptionsSaveManager("Произошла ошибка чтения из файла", exp);
        }
        return fileBackedTaskManager;
    }


    @Override
    public Task createTask(Task task) {
        task = super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic = super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask = super.createSubtask(subtask);
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
