package managers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import status.Status;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasks;
    protected Map<Integer, Epic> epics;
    protected Map<Integer, Subtask> subtasks;
    protected HistoryManager historyManager; // бращается к менеджеру истории через
    // интерфейс HistoryManager и использует реализацию, которую возвращает метод getDefaultHistory.
    // Поле для хранения отсортированных задач
    private final TreeSet<Task> prioritizedTasks;
    protected int taskId = 0;

    public InMemoryTaskManager(HistoryManager historyManager) {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        this.historyManager = historyManager;
        // Инициализация TreeSet с компаратором
        this.prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())));
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    // Вспомогательный метод для обновления TreeSet
    private void updatePrioritizedTasksAdd(Task task) {
        if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
    }

    private void updatePrioritizedTasksRemove(Task task) {
        if (task.getStartTime() != null) {
                prioritizedTasks.remove(task);
        }
    }

    public int generateTaskId() {
        return ++taskId;
    }

    // Получение списка всех задач
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getSubtaskList(Epic epic) {
        if (!epics.containsValue(epic)) {
            System.out.println("Такого эпика не существует");
            return null;
        }
        List<Subtask> list = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtasks()) {
            list.add(subtasks.get(subtaskId));
        }
        return list;
    }

    // Удаление всех задач
    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeSubtasks();
            epic.setStatus(calculateStatus(epic));
        }
    }

    // Получение по идентификатору
    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        } else {
            return null;
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        } else {
            return null;
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtasks.get(id);
    }

    // Создание задач
    @Override
    public Task createTask(Task task) {
        if (task == null) {
            return null;
        }
        if (!checkIntersectionTasks(task)) {
            task.setId(generateTaskId());
            tasks.put(task.getId(), task);

            updatePrioritizedTasksAdd(task); // добавляем задачу в TreeSet
            return task;
        } else {
            return null;
        }
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (epic == null) {
            return null;
        }
        epic.setId(generateTaskId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtask == null) {
            return null;
        }
        int saved = subtask.getEpicId();

        if (!epics.containsKey(saved)) {
            System.out.println("Такого эпика не существует");
            return null;
        }
        if (!checkIntersectionTasks(subtask)) {
            subtask.setId(generateTaskId());
            subtasks.put(subtask.getId(), subtask);

            Epic epic = epics.get(saved);
            epic.addSubtask(subtask.getId());
            updatePrioritizedTasksAdd(subtask); // добавляем задачу в TreeSet
            epic.setStatus(calculateStatus(epic));
            return subtask;
        } else {
            return null;
        }
    }

    // Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void updateTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Передана пустая задача");
        }
        if (!tasks.containsKey(task.getId())) {
            throw new NoSuchElementException("Задача с ID " + task.getId() + " не найдена");
        }
        if (task.getStatus() == null) {
            task.setStatus(Status.NEW);
        }

        Task saveTask = getTaskById(task.getId());
        updatePrioritizedTasksRemove(task);
        deleteTaskById(task.getId());

        if (!checkIntersectionTasks(task)) {
            tasks.put(task.getId(), task);
            updatePrioritizedTasksAdd(task);
        } else {
            tasks.put(task.getId(), saveTask);
            updatePrioritizedTasksAdd(saveTask);
            System.out.println("есть пересечения с другими задачами");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null) {
            System.out.println("Передан пустой эпик");
            return;
        }
        if (!epics.containsKey(epic.getId())) {
            System.out.println("Некорректный номер эпика");
            return;
        }
        Epic saved = epics.get(epic.getId());
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask == null) {
            System.out.println("Передана пустая подзадача");
            return;
        }
        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Некорректный номер подзадачи");
            return;
        }
        int epicId = subtask.getEpicId();
        if (!epics.containsKey(epicId)) {
            System.out.println("Подзадача имеет некорректный номер эпика");
            return;
        }

        Epic epic = epics.get(epicId);
        List<Integer> epicSubtaskList = epic.getSubtasks();
        if (!epicSubtaskList.contains(subtask.getId())) {
            System.out.println("Неправильно указан эпик в подзадаче");
            return;
        }

        if (subtask.getStatus() == null) {
            subtask.setStatus(Status.NEW);
        }

        Subtask saveSubtask = getSubtaskById(subtask.getId());
        updatePrioritizedTasksRemove(subtask);
        deleteSubtaskById(saveSubtask.getId());

        if (!checkIntersectionTasks(subtask)) {
            subtasks.put(subtask.getId(), subtask);
            epic.addSubtask(subtask.getId());
            updatePrioritizedTasksAdd(subtask); // добавляем задачу в TreeSet
            epic.setStatus(calculateStatus(epic));
        } else {
            subtasks.put(saveSubtask.getId(), saveSubtask);
            epic.addSubtask(saveSubtask.getId());
            updatePrioritizedTasksAdd(saveSubtask);
            epic.setStatus(calculateStatus(epic));
            System.out.println("имеется пересечение задач по времени, задача не может быть выполнена");
        }
    }

    // Удаление по идентификатору
    @Override
    public void deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Задачи с таким id не существует");
            return;
        }

        Task task = tasks.get(id);
        updatePrioritizedTasksRemove(task); // удаляем задачу в TreeSet
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпика с таким id не существует");
            return;
        }
        Epic saved = epics.get(id);

        for (Integer subTaskId : saved.getSubtasks()) {
            subtasks.remove(subTaskId);
            historyManager.remove(subTaskId);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Подзадачи с таким id не существует");
            return;
        }
        Subtask subtask = subtasks.get(id);

        int savedEpicId = subtask.getEpicId();
        Epic savedEpic = epics.get(savedEpicId);

        updatePrioritizedTasksRemove(subtask); // удаляем задачу в TreeSet
        subtasks.remove(id);
        historyManager.remove(id);
        savedEpic.deleteSubtask(id);

        savedEpic.setStatus(calculateStatus(savedEpic));
    }

    // пересчёт статуса эпика
    private Status calculateStatus(Epic epic) {
        updateEpicTime(epic);
        List<Integer> subtaskList = epic.getSubtasks();
        if (subtaskList.isEmpty()) {
            return Status.NEW;
        }
        int newStatus = 0;
        int doneStatus = 0;
        for (Integer subtaskId : subtaskList) {
            if (subtasks.get(subtaskId).getStatus().equals(Status.NEW)) {
                newStatus++;
            }
            if (subtasks.get(subtaskId).getStatus().equals(Status.DONE)) {
                doneStatus++;
            }
        }
        if (newStatus == subtaskList.size()) {
            return Status.NEW;
        }
        if (doneStatus == subtaskList.size()) {
            return Status.DONE;
        }
        return Status.IN_PROGRESS;
    }

    // перерасчет времен эпика
    private void updateEpicTime(Epic epic) {
        if (epic == null) {
            return;
        }

        // Получаем список подзадач, принадлежащих этому эпику
        List<Subtask> subtaskList = getSubtaskList(epic);

        if (subtaskList.isEmpty()) {
            // Если нет подзадач, сбрасываем время эпика
            epic.setStartTime(null);
            epic.setDuration(Duration.ZERO);
            epic.setEndTime(null);
            return;
        }

        // Вычисляем startTime как самое раннее время начала подзадач
        Instant epicStartTime = subtaskList.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(Instant::compareTo)
                .orElse(null);

        // Вычисляем endTime как самое позднее время окончания подзадач
        Instant epicEndTime = subtaskList.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(Instant::compareTo)
                .orElse(null);

        // Вычисляем duration как сумму длительностей всех подзадач
        Duration epicDuration = subtaskList.stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);

        // Устанавливаем рассчитанные значения в эпик
        epic.setStartTime(epicStartTime);
        epic.setEndTime(epicEndTime);
        epic.setDuration(epicDuration);
    }

    public boolean checkIntersectionTasks(Task task) {
        if (task.getStartTime() == null || task.getEndTime() == null) {
            System.out.println("Не задана длительность задачи по айди: " + task.getId());
            return false; // Если нет времени, пересечения невозможны
        }

        return getPrioritizedTasks().stream().anyMatch(t -> {
            if (t.getStartTime() == null || t.getEndTime() == null) {
                System.out.println("Не задано время старта задачи по айди: " + task.getId());
                return false; // Пропускаем задачи без времени
            }
            // Проверяем пересечение временных интервалов
            return task.getStartTime().isBefore(t.getEndTime()) && task.getEndTime().isAfter(t.getStartTime());
        });
    }

    // получение списка истории
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}







