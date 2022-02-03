package serviceclasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import enums.Status;

public class TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    //-------Task-------
    public void addTask(Task task) {
        String taskTitle = task.getTitle();
        int taskId = task.getId();

        if (!tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            System.out.printf("Task '%s' was added with id: %d.%n", taskTitle, taskId);
        } else {
            System.out.printf("Task with id: %d already exists.%n", taskId);
        }
    }

    public void updateTask(Task task) {
        int taskId = task.getId();

        if (tasks.containsKey(taskId)) {
            tasks.put(taskId, task);
            System.out.printf("Task with id: %d was updated.%n", taskId);
        } else {
            System.out.printf("Task with id: %d doesn't exist and cannot be updated.%n", taskId);
        }
    }

    public Task getTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.get(taskId);
        } else {
            System.out.printf("Task with id: %d doesn't exist.%n", taskId);
            return null;
        }
    }

    public void deleteTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
            System.out.printf("Task with id: %d was removed.%n", taskId);
        } else {
            System.out.printf("Task with id: %d doesn't exist.%n", taskId);
        }
    }

    public List<Task> getAllTasks() {
        if (!tasks.values().isEmpty()) {
            return new ArrayList<>(tasks.values());
        } else {
            System.out.println("There aren't existed tasks.");
            return null;
        }
    }

    public void removeAllTasks() {
        tasks.clear();
        System.out.println("All tasks were removed.");
    }

    //------Subtask----
    public void addSubtaskToEpic(Subtask subtask, int epicId) {
        Epic epicForUpdate = getEpicById(epicId);
        List<Integer> subtaskIds = epicForUpdate.getSubtaskIds();
        int subtaskId = subtask.getId();

        subtasks.put(subtaskId, subtask);
        System.out.printf("Subtask '%s' was created with id: %d.%n", subtask.getTitle(), subtaskId);
        if (!subtaskIds.contains(subtaskId)) {
            addOrRemoveSubtaskFromEpic(epicForUpdate, subtaskId, true);
            calculateEpicStatus(epicId);

            subtask.setEpicId(epicId);
            updateSubtask(subtask);
            System.out.printf("Subtask '%s' with id: %d was added to Epic '%s'.%n",
                    subtask.getTitle(), subtaskId, epicForUpdate.getTitle());
        } else {
            System.out.printf("Epic '%s' already contains the subtask '%s' with id: %d.%n",
                    epicForUpdate.getTitle(), subtask.getTitle(), subtaskId);
        }
    }

    public Subtask getSubtaskById(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            return subtasks.get(subtaskId);
        } else {
            System.out.println("Subtask with id " + subtaskId + " doesn't exist.");
            return null;
        }
    }

    public List<Subtask> getEpicSubtasksByEpicId(int epicId) {
        List<Integer> subtaskIds = new ArrayList<>();
        List<Subtask> subtasks = new ArrayList<>();

        if (epics.containsKey(epicId)) {
            subtaskIds = epics.get(epicId).getSubtaskIds();
        }
        if (!subtaskIds.isEmpty()) {
            for (int id : subtaskIds) {
                subtasks.add(getSubtaskById(id));
            }
        } else {
            System.out.println("Epic with id " + epicId + " doesn't contain subtask.");
        }
        return subtasks;
    }

    public void updateSubtask(Subtask subtask) {
        if (subtask == null) {
            System.out.println("Received subtask is null.");
            return;
        }
        int subtaskId = subtask.getId();

        if (subtasks.containsKey(subtaskId)) {
            subtasks.put(subtaskId, subtask);
            calculateEpicStatus(subtask.getEpicId());
            System.out.printf("Subtask with id: %d was updated.%n", subtaskId);
        } else {
            System.out.printf("Subtask with id: %d doesn't exist and cannot be updated.%n", subtaskId);
        }
    }

    public List<Subtask> getAllSubtasks() {
        if (!subtasks.values().isEmpty()) {
            return new ArrayList<>(subtasks.values());
        } else {
            System.out.println("There aren't existed subtasks.");
            return null;
        }
    }

    public void deleteSubtaskById(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            int epicId = getSubtaskById(subtaskId).getEpicId();
            subtasks.remove(subtaskId);

            addOrRemoveSubtaskFromEpic(getEpicById(epicId), subtaskId, false);
            calculateEpicStatus(epicId);
            System.out.printf("Subtask with id: %d was removed.%n", subtaskId);
        } else {
            System.out.printf("Subtask with id: %d doesn't exist.%n", subtaskId);
        }
    }

    public void removeAllSubtasks() {
        subtasks.clear();
        System.out.println("All subtasks were removed.");
    }

    //--------Epic-------
    public void addEpic(Epic epic) {
        String epicTitle = epic.getTitle();
        int epicId = epic.getId();

        if (!epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            System.out.printf("Epic '%s' was added with id: %d.%n", epicTitle, epicId);
        } else {
            System.out.printf("Epic with id: %d already exists.%n", epicId);
        }
    }

    public void updateEpic(Epic epic) {
        if (epic == null) {
            System.out.println("Received epic is null.");
            return;
        }
        String epicTitle = epic.getTitle();

        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            System.out.printf("Epic '%s' was updated.%n", epicTitle);
        } else {
            System.out.printf("Epic '%s' doesn't exist and cannot be updated.%n", epicTitle);
        }
    }

    public Epic getEpicById(int epicId) {
        if (epics.containsKey(epicId)) {
            return epics.get(epicId);
        } else {
            System.out.println("Epic with id " + epicId + " doesn't exist.");
            return null;
        }
    }

    public List<Epic> getAllEpics() {
        if (!epics.values().isEmpty()) {
            return new ArrayList<>(epics.values());
        } else {
            System.out.println("There aren't existed epics.");
            return null;
        }
    }

    public void removeAllEpics() {
        epics.clear();
        System.out.println("All epics were removed.");
    }


    private void calculateEpicStatus(int epicId) {
        Epic epic = getEpicById(epicId);
        Status epicStatus = epic.getStatus();
        List<Subtask> subtasksInEpic = getEpicSubtasksByEpicId(epicId);
        Status newEpicStatus;

        if (subtasksInEpic.isEmpty()) {
            newEpicStatus = Status.NEW;
        } else {
            Set<Status> statuses = subtasksInEpic.stream()
                    .map(Task::getStatus)
                    .collect(Collectors.toSet());
            if (statuses.size() == 1 && statuses.contains(Status.DONE)) {
                newEpicStatus = Status.DONE;
            } else if (statuses.isEmpty() || (statuses.size() == 1 && statuses.contains(Status.NEW))) {
                newEpicStatus = Status.NEW;
            } else {
                newEpicStatus = Status.IN_PROGRESS;
            }
        }
        if (!epicStatus.equals(newEpicStatus)) {
            epic.setStatus(newEpicStatus);
            updateEpic(epic);
        }
    }

    private void addOrRemoveSubtaskFromEpic(Epic epic, int subtaskId, boolean isAddition) {
        List<Integer> subtaskIds = epic.getSubtaskIds();

        if (isAddition) {
            subtaskIds.add(subtaskId);
        } else {
            subtaskIds.remove(Integer.valueOf(subtaskId));
        }
        epic.setSubtaskIds(subtaskIds);
        updateEpic(epic);
    }
}
