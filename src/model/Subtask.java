package model;

import enums.TaskType;
import status.Status;

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
    public String toString() {
        return "model.Subtask{" +
                "epicId=" + getEpicId() +
                ", id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", taskStatus=" + getStatus() +
                '}';
    }
}

