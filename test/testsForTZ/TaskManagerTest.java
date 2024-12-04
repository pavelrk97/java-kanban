package testsForTZ;

import managers.TaskManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;

abstract class TaskManagerTest<T extends TaskManager> {
    @Test
    void addNewTaskShouldSaveTaskEqualsById() {

    }

    @Test
    void updateTaskShouldUpdateTaskWithSpecificatedId() {

    }

    @Test
    void epicsAreEqual() {

    }

    @Test
    void subsAreEqual() {

    }

    @Test
    void getDefaultShouldInitializeInMemoryTaskManager() {

    }

    @Test
    void getDefaultHistoryShouldInitializeInMemoryHistoryManager() {

    }

    @Test
    void InMemoryTaskManagerShouldAddTaskShouldLocateById() {

    }

    @Test
    void InMemoryTaskManagerShouldAddEpicShouldLocateById() {

    }

    @Test
    void InMemoryTaskManagerShouldAddSubShouldLocateById() {

    }

    @Test
    void noConflictWhenCreatedTaskSameID() {

    }

    @Test
    void allInTaskIsSame() {

    }

    @Test
    void historyShowsPrevious() {

    }

    @Test
    void subNotAddedToEpicWithSameId() {

    }

    @Test
    void addedTaskEqualsHistoryTask() {

    }

    @Test
    void whenDeletedFromListDeletedFromHistory() {

    }

    @Test
    void whenAddedToListAddedToHistory() {

    }

    @Test
    void deletedSubtasksHasNoId() {

    }

    @Test
    void saveAndLoadFromEmptyFile() {

    }

    // Проверить сохранение нескольких задач, загрузку нескольких задач
    @Test
    void saveAndLoadFromNonEmptyFile() {

    }

    // Проверка файлового менеджера, созданного из непустого файла
    @Test
    void loadFromNonEmptyFile() {

    }

    @Test
    void loadFromCreatedFile() throws IOException {

    }

    @Test
    void crossIntervals() {

    }

    @Test
    public void crossIntervalsBefore() {
    }

    @Test
    public void crossIntervalsAfter() {}

    @Test
    public void crossIntervalsUpdTask() {}

    @Test
    public void crossIntervalsUpdSub() {}
}
