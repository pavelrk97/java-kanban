package model;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import enums.TaskType;
import status.Status;

public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;
    private Instant startTime; // инстант тк это веб приложение и сервера могут быть в разных поясах часовых
    private Instant endTime;
    private Duration duration;


    public Task(String name, String description) {
        this(name, description, Status.NEW, Instant.now());
    }

    public Task(String name, String description, Status status) {
        this(name, description, status, Instant.now());
    }

    public Task(String name, String description, Status status, int oldId) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = oldId;
    }

    public Task(int id, String name, String description) {
        this(id, name, description, Status.NEW, Instant.now(), null);
    }

    public Task(int id, String name, String description, Status status) {
        this(id, name, description, status, Instant.now(), null);
    }

    public Task(String name, String description, Status status, Instant startTime) {
        this(name, description, status, startTime, null);
    }

    public Task(int id, String name, String description, Status status, Instant startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, Status status, Instant startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        if (Objects.isNull(duration) || Objects.isNull(startTime)) {
            return null;
        }
        return startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id && name.equals(task.name) && description.equals(task.description);
        // добавлены параметры для сравнения + сабтаок +парам епика + для епика айди сабтасок внутри
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription());
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s", getId(), TaskType.TASK, getName(), getStatus(), getDescription(), getDuration() == null ? "0" : getDuration().toMinutes(), getStartTime() == null ? "N/A" : getStartTime());
    }
}

