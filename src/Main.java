import enums.Status;
import serviceclasses.Epic;
import serviceclasses.Subtask;
import serviceclasses.Task;
import serviceclasses.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task firstTask = new Task("Test task 1", "Test description for task 1");
        Task secondTask = new Task("Test task 2", "Test description for task 2");
        taskManager.addTask(firstTask);
        taskManager.addTask(secondTask);

        Epic firstEpic = new Epic("Test epic 1", "Test description for epic 1");
        Epic secondEpic = new Epic("Test epic 2", "Test description for epic 2");
        taskManager.addEpic(firstEpic);
        taskManager.addEpic(secondEpic);

        Subtask firstSubtask = new Subtask("Test subtask 1", "First subtask for epic 1");
        Subtask secondSubtask = new Subtask("Test subtask 2", "Second subtask for epic 1");
        Subtask thirdSubtask = new Subtask("Test subtask 3", "Second subtask for epic 2");
        taskManager.addSubtaskToEpic(firstSubtask, firstEpic.getId());
        taskManager.addSubtaskToEpic(secondSubtask, firstEpic.getId());
        taskManager.addSubtaskToEpic(thirdSubtask, secondEpic.getId());

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        Task updatedFirstTask = taskManager.getTaskById(firstTask.getId());
        updatedFirstTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(updatedFirstTask);

        Subtask firstSubtaskWithInProgressStatus = taskManager.getSubtaskById(firstSubtask.getId());
        firstSubtaskWithInProgressStatus.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(firstSubtaskWithInProgressStatus);
        System.out.println(taskManager.getEpicById(firstEpic.getId()));

        Subtask thirdSubtaskWithInProgressStatus = taskManager.getSubtaskById(thirdSubtask.getId());
        thirdSubtaskWithInProgressStatus.setStatus(Status.DONE);
        taskManager.updateSubtask(thirdSubtaskWithInProgressStatus);
        System.out.println(taskManager.getEpicById(secondEpic.getId()));

        taskManager.deleteSubtaskById(thirdSubtask.getId());

        taskManager.deleteTaskById(firstTask.getId());

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllSubtasks();

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
    }
}
