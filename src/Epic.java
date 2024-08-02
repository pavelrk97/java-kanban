import java.util.HashMap;

public class Epic extends Task{
    HashMap<Integer, Subtask> epicSubTasks;

    public Epic(String name, String description, int status) {
        super(name, description, status);
        this.epicSubTasks = new HashMap<>();
    }


}
