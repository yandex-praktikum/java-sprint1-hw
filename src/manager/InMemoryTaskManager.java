package manager;

import enums.Status;
import enums.TypeOfTask;
import history.HistoryManager;
import history.InMemoryHistoryManager;
import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();//написать геттер
    private final HashMap<Integer, Task> taskMap = new HashMap<>();
    private int idCounter = 0;

    private int generateID() {//create static variable!!
        return idCounter++;
    }

    @Override
    public void createTask(Task task) {
        if (task.getId() != null) {
            System.out.println("Ошибка!!!Задача с не пустым ID");
            throw new IllegalArgumentException();
        }

        if (task.getTaskType().equals(TypeOfTask.SUBTASK)) {
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
        } else {
            task.setId(generateID());
            taskMap.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        ArrayList<SubTask> subTasks = getAllSubTaskOfEpicByEpicID(epic.getId());
        HashMap<Status, Integer> statusCounter = new HashMap<>();
        int allSubTusks = 0;

        for (SubTask subTask : subTasks) {
            Integer statusCount = statusCounter.getOrDefault(subTask.getStatus(), 0);
            statusCounter.put(subTask.getStatus(), statusCount + 1);
            allSubTusks += 1;
        }
        if (allSubTusks == 0 || statusCounter.getOrDefault(Status.NEW, 0) == allSubTusks) {
            epic.setStatus(Status.NEW);
        } else if (statusCounter.getOrDefault(Status.DONE, 0) == allSubTusks) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task.getId() == null) {
            System.out.println("Ошибка!!!Задача с пустым ID");
            return;
        }
        if (!taskMap.containsKey(task.getId())) {
            System.out.println("Задача с таким ID не существует");
            return;
        }
        if (task.getTaskType().equals(TypeOfTask.SUBTASK)) {
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


    @Override
    public void deleteTaskByID(int id) {
        if (!taskMap.containsKey(id)) {
            System.out.println("Задача с таким ID не существует");
            return;
        }
        Task task = taskMap.get(id);
        if (task.getTaskType().equals(TypeOfTask.SUBTASK)) {
            SubTask subTask = (SubTask) task;
            int epicID = subTask.getEpicID();
            Epic epic = (Epic) taskMap.get(epicID);
            epic.getSubTaskIDs().remove(subTask.getId());
            updateEpicStatus(epic);
        } else if (task.getTaskType().equals(TypeOfTask.EPIC)) {
            Epic epic = (Epic) task;
            if (!epic.getSubTaskIDs().isEmpty()) {
                System.out.println("Нельзя удалить эпик с подзадачами");
                return;
            }
        }
        taskMap.remove(id);
    }

    @Override
    public void deleteAllFromType(TypeOfTask taskType) {
        List<Task> taskList = new ArrayList<>(taskMap.values());
        switch (taskType) {
            case SUBTASK:
                System.out.println("Для удаления всех задач с типом 'подзадача' требуется ID эпика");
                break;
            case EPIC:
                for (Task tasks : taskList) {
                    if (tasks.getTaskType().equals(TypeOfTask.EPIC)) {
                        Epic epic = (Epic) tasks;
                        ArrayList<Integer> subTaskIDs = epic.getSubTaskIDs();
                        for (Integer subTaskID : subTaskIDs) {
                            taskMap.remove(subTaskID);
                        }
                        taskMap.remove(tasks.getId());
                    }
                }
                break;
            case TASK:
                for (Task task : taskList) {
                    if (task.getTaskType().equals(TypeOfTask.TASK)) {
                        taskMap.remove(task.getId());
                    }
                }
                break;
            default:
                System.out.println("Данный тип задачи не существует");
        }
    }

    @Override
    public void deleteAllSubTasksFromCurrentEpic(int epicID) {
        Task task = taskMap.get(epicID);
        if (!task.getTaskType().equals(TypeOfTask.EPIC)) {
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

    @Override
    public List<Task> getAllFromType(TypeOfTask taskType) {
        List<Task> result = new ArrayList<>();
        switch (taskType) {
            case TASK:
                for (Task task : taskMap.values()) {
                    if (task.getTaskType().equals(TypeOfTask.TASK)) {
                        result.add(task);
                    }
                }
                break;
            case EPIC:
                for (Task task : taskMap.values()) {
                    if (task.getTaskType().equals(TypeOfTask.EPIC)) {
                        result.add(task);
                    }
                }
                break;
            case SUBTASK:
                System.out.println("Для получения всех задач с типом 'подзадача' требуется ID эпика");
                break;
            default:
                System.out.println("Данный тип задачи не существует");
        }
        return result;
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public Task getTaskByID(int id) {
        if (!taskMap.containsKey(id)) {
            System.out.println("Задача с таким ID не существует");
            throw new IllegalArgumentException();
        }
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public ArrayList<SubTask> getAllSubTaskOfEpicByEpicID(int epicID) {
        if (!taskMap.containsKey(epicID)) {
            System.out.println("Задача с таким ID не существует");
            throw new IllegalArgumentException();
        }
        Task task = taskMap.get(epicID);
        if (!task.getTaskType().equals(TypeOfTask.EPIC)) {
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

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
