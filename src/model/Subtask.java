package model;

import enums.TaskType;
import status.Status;

import java.util.Objects;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, int epicId) {
        super(id, name, description);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Subtask task = (Subtask) object;
        return super.equals(object) && Objects.equals(this.epicId, task.epicId);
        // добавить параметры для сравнения + сабт +парам епика + для епика айди сабтасок внутри
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d", getId(), TaskType.SUBTASK, getName(), getStatus(), getDescription(), getEpicId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}

