package manager;

import history.HistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import enums.TypeOfTask;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void createTask(Task task);

    void updateEpicStatus(Epic epic);

    void updateTask(Task task);

    void deleteTaskByID(int id);

    void deleteAllFromType(TypeOfTask taskType);

    void deleteAllSubTasksFromCurrentEpic(int epicID);

    List<Task> getAllFromType(TypeOfTask taskType);

    List<Task> getAllTask();

    ArrayList<SubTask> getAllSubTaskOfEpicByEpicID(int epicID);

    Task getTaskByID(int id);

    HistoryManager getHistoryManager();
}
