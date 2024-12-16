package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;
import type.adapters.DurationAdapter;
import type.adapters.LocalDateTimeAdapter;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class TaskHandler extends BaseHttpHandler {

    protected final TaskManager manager;
    protected final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public TaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange h) {
        try {
            String requestMethod = h.getRequestMethod();
            String requestPath = h.getRequestURI().getPath();
            String body = new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            if (Pattern.matches("/tasks/", requestPath) || Pattern.matches("/tasks", requestPath)) {
                switch (requestMethod) {
                    case "GET":
                        sendText(h, taskListSerialize(manager.getAllTasks()), 200);
                        break;
                    case "POST":
                        if (body.isEmpty() || body.isBlank()) {
                            sendText(h, "request body is empty", 400);
                        } else if (!body.contains("\"id\"")) {
                            addTask(h, body);
                        } else {
                            updateTask(h, body);
                        }
                        break;
                }

            } else if (Pattern.matches("/tasks/\\d+", requestPath)
                    || Pattern.matches("/tasks/\\d+/", requestPath)) {
                Optional<Integer> id = getId(requestPath);
                if (id.isPresent()) {
                    switch (requestMethod) {
                        case "GET" -> getTask(h, id.get());
                        case "DELETE" -> deleteTask(h, id.get());
                    }
                }

            } else {
                sendText(h, "Unknown request", 404);
            }

        } catch (Exception e) {
            sendInternalError(h);
        }
    }

    private void addTask(HttpExchange h, String body) {
        try {
            Task addedTask = manager.addNewTask(gson.fromJson(body, Task.class));
            if (Objects.nonNull(addedTask)) {
                sendText(h, taskSerialize(addedTask), 201);
            } else {
                sendText(h, "Task time overlaps with existing tasks", 406);
            }
        } catch (Exception e) {
            sendInternalError(h);
        }
    }

    private void updateTask(HttpExchange h, String body) {
        try {
            Task updatedTask = manager.updateTask(gson.fromJson(body, Task.class));
            if (Objects.nonNull(updatedTask)) {
                sendText(h, taskSerialize(updatedTask), 201);
            } else {
                sendText(h, "Task time overlaps with existing tasks", 406);
            }
        } catch (Exception e) {
            sendInternalError(h);
        }
    }

    private void getTask(HttpExchange h, Integer taskId) {
        try {
            Task task = manager.getTask(taskId);
            if (Objects.isNull(task)) {
                sendText(h, "Task with id " + taskId + " is not exist", 404);
            } else {
                sendText(h, taskSerialize(task), 200);
            }
        } catch (Exception e) {
            sendInternalError(h);
        }
    }

    private void deleteTask(HttpExchange h, Integer taskId) {
        try {
            Task delTask = manager.deleteTask(taskId);
            if (Objects.nonNull(delTask)) {
                String response = "Successful remove task: " + "id: "
                        + delTask.getId() + ", type: " + delTask.getType();
                sendText(h, response, 200);
            } else {
                sendText(h, "Task with id " + taskId + " does not exist", 404);
            }
        } catch (Exception e) {
            sendInternalError(h);
        }
    }

    protected String taskSerialize(Task task) {
        return gson.toJson(task);
    }

    protected String taskListSerialize(List<? extends Task> tasks) {
        return gson.toJson(tasks);
    }

    protected Optional<Integer> getId(String requestPath) {
        String[] pathParts = requestPath.split("/");
            try {
                return Optional.of(Integer.parseInt(pathParts[2]));
            } catch (NumberFormatException exception) {
                return Optional.empty();
            }
    }
}
