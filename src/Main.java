import enums.Status;
import manager.Managers;
import manager.TaskManager;
import tasks.*;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        Task task = new Task("Tasks.Tasks.Task", "desc", Status.IN_PROGRESS);
        manager.createTask(task);

        Task task1 = new Task("Task1", "desc", Status.IN_PROGRESS);
        manager.createTask(task1);

        Epic epic = new Epic("epic1", "description");
        manager.createTask(epic);

        SubTask subTask1 = new SubTask("sub1", "des1", Status.NEW, epic.getId());
        manager.createTask(subTask1);

        SubTask subTask2 = new SubTask("sub2", "des2", Status.NEW, epic.getId());
        manager.createTask(subTask2);

        Epic epic2 = new Epic("epic2", "description");
        manager.createTask(epic2);

        SubTask subTask = new SubTask("sub", "des", Status.NEW, epic2.getId());
        manager.createTask(subTask);

        manager.getTaskByID(2);
        manager.getTaskByID(3);
        manager.getTaskByID(0);
        manager.getTaskByID(1);
        manager.getTaskByID(4);
        manager.getTaskByID(5);
        manager.getTaskByID(6);
        manager.getTaskByID(0);
        manager.getTaskByID(1);
        manager.getTaskByID(2);
        manager.getTaskByID(3);
        manager.getTaskByID(4);

        for (Task t:manager.getHistoryManager().getHistory()) {
            System.out.println(t.getId());
        }

    }
}
