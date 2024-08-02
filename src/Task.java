import java.util.Objects;

public class Task {
    private static int idCounter = 0;

    private String name;
    private String description;
    private Status status;
    private int id;

    public Task(String name, String description, int status) {
        this.name = name;
        this.description = description;
        this.id = ++idCounter;
        switch (status) {
            case 1:
                this.status = Status.NEW;
                break;
            case 2:
                this.status = Status.IN_PROGRESS;
                break;
            case 3:
                this.status = Status.DONE;
                break;
            default:
                this.status = Status.NEW;
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }



    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null) {
            hash = hash + name.hashCode();
        }
        hash = hash * 31;
        if (description != null) {
            hash = hash + description.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                status == task.status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}
