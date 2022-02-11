package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private int idCounter = 0;

    public void printTaskMap() {
        for (Task task : taskMap.values()) {
            System.out.println(task);
        }

    }

    private int generateID() {
        return ++idCounter;
    }

    public int createTask(Task task) {
        if (task.getId() != null) {
            System.out.println("Ошибка!!!Задача с не пустым ID");
            throw new IllegalArgumentException();
        }

        if (task.getTaskType().equals("subtask")) {//генерировать в список ID эпиков
            SubTask subTask = (SubTask) task;
            int epicID = subTask.getEpicID();
            if (!taskMap.containsKey(epicID)) {
                System.out.println("Передан epicID, которого нет в базе");
                throw new IllegalArgumentException();
            }

            subTask.setId(generateID());
            taskMap.put(subTask.getId(), subTask);

            Epic epic = (Epic) taskMap.get(epicID);

            ArrayList<Integer> subTaskIds = epic.getSubTaskIDs();
            subTaskIds.add(subTask.getId());
            updateEpicStatus(epic);
            return subTask.getId();
        } else {
            task.setId(generateID());
            taskMap.put(task.getId(), task);
            return task.getId();
        }
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<SubTask> subTasks = getAllSubTaskOfEpicByEpicID(epic.getId());
        HashMap<String, Integer> statusCounter = new HashMap<>();
        int allSubTusks = 0;

        for (SubTask subTask : subTasks) {
            Integer statusCount = statusCounter.getOrDefault(subTask.getStatus(), 0);
            statusCounter.put(subTask.getStatus(), statusCount + 1);
            allSubTusks += 1;
        }
        if (allSubTusks == 0 || statusCounter.getOrDefault("NEW", 0) == allSubTusks) {
            epic.setStatus("NEW");
        } else if (statusCounter.getOrDefault("DONE", 0) == allSubTusks) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }

    public void updateTask(Task task) {
        if (task.getId() == null) {
            System.out.println("Ошибка!!!Задача с пустым ID");
            return;
        }
        if (!taskMap.containsKey(task.getId())) {
            System.out.println("Задача с таким ID не существует");
            return;
        }
        if (task.getTaskType().equals("subtask")) {
            SubTask subTask = (SubTask) task;
            int epicID = subTask.getEpicID();

            if (!taskMap.containsKey(epicID)) {
                System.out.println("Передан epicID, которого нет в базе");
                return;
            }
            taskMap.put(subTask.getId(), subTask);
            Epic epic = (Epic) taskMap.get(epicID);
            updateEpicStatus(epic);
        } else {
            taskMap.put(task.getId(), task);
        }
    }

    public void deleteTaskByID(int id) {
        if (!taskMap.containsKey(id)) {
            System.out.println("Задача с таким ID не существует");
            return;
        }
        Task task = taskMap.get(id);
        if (task.getTaskType().equals("subtask")) {
            SubTask subTask = (SubTask) task;
            int epicID = subTask.getEpicID();
            Epic epic = (Epic) taskMap.get(epicID);
            epic.getSubTaskIDs().remove(subTask.getId());
            updateEpicStatus(epic);
        } else if (task.getTaskType().equals("epic")) {
            Epic epic = (Epic) task;
            if (!epic.getSubTaskIDs().isEmpty()) {
                System.out.println("Нельзя удалить эпик с подзадачами");
                return;
            }
        }
        taskMap.remove(id);
    }

    public void deleteAllFromType(String taskType) {
        List<Task> taskList = new ArrayList<>(taskMap.values());
        switch (taskType) {
            case "subtask":
                System.out.println("Для удаления всех задач с типом 'подзадача' требуется ID эпика");
                break;
            case "epic":
                for (Task tasks : taskList) {
                    if (tasks.getTaskType().equals("epic")) {
                        Epic epic = (Epic) tasks;
                        ArrayList<Integer> subTaskIDs = epic.getSubTaskIDs();
                        for (Integer subTaskID : subTaskIDs) {
                            taskMap.remove(subTaskID);
                        }
                        taskMap.remove(tasks.getId());
                    }
                }
                break;
            case "task":
                for (Task task : taskList) {
                    if (task.getTaskType().equals("task")) {
                        taskMap.remove(task.getId());
                    }
                }
                break;
            default:
                System.out.println("Данный тип задачи не сущесвует");
        }
    }

    public void deleteAllSubTasksFromCurrentEpic(int epicID) {
        Task task = taskMap.get(epicID);
        if (!task.getTaskType().equals("epic")) {
            System.out.println("Задача с данным ID не является эпиком");
            return;
        }
        Epic epic = (Epic) task;
        ArrayList<Integer> subTaskIDs = new ArrayList<>(epic.getSubTaskIDs());
        for (Integer subTaskID : subTaskIDs) {
            taskMap.remove(subTaskID);
            epic.getSubTaskIDs().remove(subTaskID);
        }
    }

    public List<Task> getAllFromType(String taskType) {
        List<Task> result = new ArrayList<>();
        switch (taskType) {
            case "task":
                for (Task task : taskMap.values()) {
                    if (task.getTaskType().equals("task")) {
                        result.add(task);
                    }
                }
                break;
            case "epic":
                for (Task task : taskMap.values()) {
                    if (task.getTaskType().equals("epic")) {
                        result.add(task);
                    }
                }
                break;
            case "subtask":
                System.out.println("Для получения всех задач с типом 'подзадача' требуется ID эпика");
                break;
            default:
                System.out.println("Данный тип задачи не сущесвует");
        }
        return result;
    }

    List<Task> getAllTask() {
        return new ArrayList<>(taskMap.values());
    }

    public Task getTaskByID(int id) {
        if (!taskMap.containsKey(id)) {
            System.out.println("Задача с таким ID не существует");
            throw new IllegalArgumentException();
        }
        return taskMap.get(id);
    }

    public ArrayList<SubTask> getAllSubTaskOfEpicByEpicID(int epicID) {
        if (!taskMap.containsKey(epicID)) {
            System.out.println("Задача с таким ID не существует");
            throw new IllegalArgumentException();
        }
        Task task = taskMap.get(epicID);
        if (!task.getTaskType().equals("epic")) {
            System.out.println("Нельзя получить список задач по ID задачи или подзадачи");
            throw new IllegalArgumentException();
        }
        Epic epic = (Epic) task;
        ArrayList<Integer> subTaskIDs = epic.getSubTaskIDs();
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (Integer subTaskID : subTaskIDs) {
            SubTask subTask = (SubTask) taskMap.get(subTaskID);
            subTasks.add(subTask);
        }
        return subTasks;
    }
}
