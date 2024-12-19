package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class AbstractTaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Duration duration;
    protected LocalDateTime time1;
    protected LocalDateTime time2;
    protected LocalDateTime time3;
    protected LocalDateTime time4;

    @BeforeEach
    void init() {
        duration = Duration.ofMinutes(10);
        time1 = LocalDateTime.of(2024, 10, 10, 10, 10);
        time2 = LocalDateTime.of(2024, 10, 10, 11, 10);
        time3 = LocalDateTime.of(2024, 10, 10, 12, 10);
        time4 = LocalDateTime.of(2024, 10, 10, 13, 10);
    }

    @Test
    void addNewTaskShouldSaveTask() {
        // prepare
        Task task = new Task("task1", "description_1", duration, time1);
        Task expectedTask = new Task(0, "task1", "description_1", TaskStatus.NEW, duration, time1);

        // do
        Task actualTask = taskManager.addNewTask(task);

        // check
        assertNotNull(actualTask);
        assertNotNull(actualTask.getId());
        assertEquals(expectedTask, actualTask);
    }

    @Test
    void addNewTaskShouldSaveEpic() {
        // prepare
        Epic epic = new Epic("epic_1", "epic_description_1", duration, time1);
        Epic expectedEpic = new Epic(0, "epic_1", "epic_description_1", TaskStatus.NEW, duration, time1);

        // do
        Epic actualEpic = taskManager.addNewTask(epic);

        // check
        assertNotNull(actualEpic);
        assertNotNull(actualEpic.getId());
        assertEquals(expectedEpic, actualEpic);
    }

    @Test
    void addNewTaskShouldNotSaveEpicAsTask() {
        // prepare
        Task epic = new Epic("epic_1", "epic_description_1");

        // do
        Task actualEpic = taskManager.addNewTask(epic);

        // check
        assertNull(actualEpic);
    }

    @Test
    void addNewTaskShouldNotSaveSubtaskAsTask() {
        // prepare
        Task subtask = new Subtask("sub_1", "sub_description_1", 0);

        // do
        Task actualSubtask = taskManager.addNewTask(subtask);

        // check
        assertNull(actualSubtask);
    }

    @Test
    void addNewTaskShouldSaveSubtaskWithExistEpic() {
        // prepare
        Epic epic = new Epic("Epic 1", "Epic Description", duration, time1);
        taskManager.addNewTask(epic);
        Subtask subtask = new Subtask(1, "subtask_1", "subtask_description_1",
                TaskStatus.NEW, duration, time2, 0);
        Subtask expectedSubtask = new Subtask(1, "subtask_1", "subtask_description_1",
                TaskStatus.NEW, duration, time2, 0);

        // do
        Subtask actualSubtask = taskManager.addNewTask(subtask);

        // check
        assertNotNull(actualSubtask);
        assertNotNull(actualSubtask.getId());
        assertEquals(expectedSubtask, actualSubtask);
    }

    @Test
    void updateTaskShouldUpdateTaskWithSpecifiedId() {
        // prepare
        Task task = new Task("task_1", "description_1", duration, time1);
        Task savedTask = taskManager.addNewTask(task);
        Task updatedTask = new Task(savedTask.getId(), "task_1_updated", "description_1_updated",
                TaskStatus.NEW, duration, time1);

        Task expectedUpdatedTask = new Task(savedTask.getId(), "task_1_updated", "description_1_updated",
                TaskStatus.NEW, duration, time1);

        // do
        Task actualUpdatedTask = taskManager.updateTask(updatedTask);

        // check
        assertEquals(expectedUpdatedTask, actualUpdatedTask);
    }

    @Test
    void updateTaskShouldUpdateEpicWithSpecifiedId() {
        // prepare
        Epic epic = new Epic("epic_1", "epic_description_1", duration, time1);
        Epic savedEpic = taskManager.addNewTask(epic);
        Epic updatedEpic = new Epic(savedEpic.getId(), "epic_1_updated", "epic_description_1_updated",
                TaskStatus.NEW, duration, time1);

        Epic expectedUpdatedEpic = new Epic(savedEpic.getId(), "epic_1_updated",
                "epic_description_1_updated", TaskStatus.NEW, duration, time1);

        // do
        Epic actualUpdatedEpic = taskManager.updateTask(updatedEpic);

        // check
        assertEquals(expectedUpdatedEpic, actualUpdatedEpic);
    }

    @Test
    void updateTaskShouldUpdateSubtaskWithSpecifiedId() {
        // prepare
        Epic epic = new Epic("epic_1", "epic_description_1", TaskStatus.NEW, duration, time1);
        taskManager.addNewTask(epic);
        Subtask subtask = new Subtask("subtask_1", "subtask_description_1",
                TaskStatus.NEW, duration, time2, 0);
        Subtask savedSubtask = taskManager.addNewTask(subtask);
        Subtask updatedSubtask = new Subtask(savedSubtask.getId() ,"subtask_1_updated",
                "subtask_description_1_updated", TaskStatus.NEW, duration, time3, 0);

        Subtask expectedUpdatedSubtask = new Subtask(savedSubtask.getId(), "subtask_1_updated",
                "subtask_description_1_updated", TaskStatus.NEW, duration, time3, 0);

        // do
        Subtask actualUpdatedSubtask = taskManager.updateTask(updatedSubtask);

        // check
        assertEquals(expectedUpdatedSubtask, actualUpdatedSubtask);
    }

    @Test
    void updateTaskShouldUpdateSubtaskEpicId() {
        // prepare
        Epic epic1 = new Epic("epic_1", "epic_description_1", duration, time1);
        Epic epic2 = new Epic("epic_2", "epic_description_2", duration, time2);

        taskManager.addNewTask(epic1);
        taskManager.addNewTask(epic2);

        Subtask subtask = new Subtask("subtask_1", "subtask_description_1", duration, time3, 0);
        Subtask updateSubtask = new Subtask(2,"subtask_1", "subtask_description_1",
                TaskStatus.NEW, duration, time3,1);

        Subtask savedSubtask = taskManager.addNewTask(subtask);

        // do
        // check
        assertEquals(0, savedSubtask.getEpicId());
        assertEquals(2, epic1.getEpicSubtasksId().get(0));
        Subtask updatedSubtask = taskManager.updateTask(updateSubtask);
        assertEquals(1, updatedSubtask.getEpicId());
        assertEquals(2, epic2.getEpicSubtasksId().get(0));
    }

    @Test
    void refreshEpicStatusShouldUpdateEpicStatusSubtasksInProgress() {
        // prepare
        Epic epic = new Epic("epic_1", "epic_description_1", duration, time1);
        taskManager.addNewTask(epic);
        Subtask subtask1 = new Subtask("subtask_1", "subtask_description_1", duration, time2, 0);
        Subtask subtask2 = new Subtask("subtask_2", "subtask_description_2", duration, time3, 0);
        Subtask subtask3 = new Subtask("subtask_3", "subtask_description_3", duration, time4, 0);
        taskManager.addNewTask(subtask1);
        taskManager.addNewTask(subtask2);
        taskManager.addNewTask(subtask3);

        // do
        // check
        assertEquals(TaskStatus.NEW, epic.getStatus());
        taskManager.updateTask(new Subtask(1, "subtask_u_1", "subtask_description_u_1",
                TaskStatus.IN_PROGRESS, duration, time2));
        taskManager.updateTask(new Subtask(2, "subtask_u_2", "subtask_description_u_2",
                TaskStatus.IN_PROGRESS, duration, time3));
        taskManager.updateTask(new Subtask(3, "subtask_u_3", "subtask_description_u_3",
                TaskStatus.IN_PROGRESS, duration, time4));
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void deleteTaskShouldDeleteTaskWithSpecifiedId() {
        // prepare
        Task task = new Task("task_1", "description_1", duration, time1);
        Task savedTask = taskManager.addNewTask(task);

        // do
        // check
        assertNotNull(taskManager.getTask(savedTask.getId()));
        taskManager.deleteTask(savedTask.getId());
        assertNull(taskManager.getTask(savedTask.getId()));
    }

    @Test
    void deleteTaskShouldDeleteEpicWithSpecifiedId() {
        // prepare
        Epic epic = new Epic("epic_1", "epic_description_1", duration, time1);
        Epic savedEpic = taskManager.addNewTask(epic);

        // do
        // check
        assertNotNull(taskManager.getEpic(savedEpic.getId()));
        taskManager.deleteEpic(savedEpic.getId());
        assertNull(taskManager.getEpic(savedEpic.getId()));
    }

    @Test
    void deleteTaskShouldDeleteSubtaskWithSpecifiedId() {
        // prepare
        Epic epic = new Epic("epic_1", "epic_description_1", duration, time1);
        taskManager.addNewTask(epic);
        Subtask subtask = new Subtask("subtask_1", "subtask_description_1", duration, time2, 0);
        Subtask savedSubtask = taskManager.addNewTask(subtask);

        // do
        // check
        assertNotNull(taskManager.getSubtask(savedSubtask.getId()));
        taskManager.deleteSubtask(savedSubtask.getId());
        assertNull(taskManager.getSubtask(savedSubtask.getId()));
    }

    @Test
    void getTaskShouldGetTask() {
        // prepare
        Task task = new Task("task_1", "description_1", TaskStatus.NEW, duration, time1);
        Task expectedTask = new Task(0,"task_1", "description_1", TaskStatus.NEW, duration, time1);

        // do
        Task actualTask = taskManager.addNewTask(task);

        // check
        assertNotNull(taskManager.getTask(actualTask.getId()));
        assertEquals(expectedTask, taskManager.getTask(actualTask.getId()));
    }

    @Test
    void getTaskShouldGetEpic() {
        // prepare
        Epic epic = new Epic("epic_1", "epic_description_1", duration, time1);
        Epic expectedEpic = new Epic(0,"epic_1", "epic_description_1",
                TaskStatus.NEW, duration, time1);

        // do
        Epic actualEpic = taskManager.addNewTask(epic);

        // check
        assertNotNull(taskManager.getEpic(actualEpic.getId()));
        assertEquals(expectedEpic, taskManager.getEpic(actualEpic.getId()));
    }

    @Test
    void getTaskShouldGetSubtask() {
        // prepare
        Epic epic = new Epic("epic_1", "epic_description_1", duration, time1);
        taskManager.addNewTask(epic);
        Subtask subtask = new Subtask("subtask_1", "subtask_description_1", duration, time2, 0);
        Subtask expectedSubtask = new Subtask(1,"subtask_1", "subtask_description_1", duration, time2);

        // do
        Subtask actualSubtask = taskManager.addNewTask(subtask);

        // check
        assertNotNull(taskManager.getSubtask(actualSubtask.getId()));
        assertEquals(expectedSubtask, taskManager.getSubtask(actualSubtask.getId()));
    }
}