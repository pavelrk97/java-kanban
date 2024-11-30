package model;

import enums.TaskType;
import status.Status;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subtasks = new ArrayList<>();
    private Instant endTime;

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, int idOld) {
        super(name, description);
        this.setId(idOld);
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Epic(String name, String description, Status status, Instant startTime) {
        super(name, description, status, startTime);
    }

    public List<Integer> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Integer subtaskId) {
        subtasks.add(subtaskId);
    }

    public void deleteSubtask(Integer subtaskId) {
        subtasks.remove(subtaskId);
    }

    public void removeSubtasks() {
        subtasks.clear();
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }

    public void setEpicEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Epic epic = (Epic) object;
        return super.equals(object) && Objects.equals(this.subtasks, epic.subtasks);
        // добавить параметры для сравнения + сабт +парам епика + для епика айди сабтасок внутри
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s, %s", getId(), TaskType.EPIC, getName(), getStatus(),
                getDescription(), getSubtasks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }
}
