import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        Task task = new Task("Tasks.Tasks.Task", "desc", "IN_PROGRESS");
        manager.createTask(task);

        Task task1 = new Task("Task1", "desc", "IN_PROGRESS");
        manager.createTask(task1);

        Epic epic = new Epic("epic1", "description");
        manager.createTask(epic);

        SubTask subTask1 = new SubTask("sub1", "des1", "NEW", epic.getId());
        manager.createTask(subTask1);

        SubTask subTask2 = new SubTask("sub2", "des2", "NEW", epic.getId());
        manager.createTask(subTask2);

        Epic epic2 = new Epic("epic2", "description");
        manager.createTask(epic2);

        SubTask subTask = new SubTask("sub", "des", "NEW", epic2.getId());
        manager.createTask(subTask);
        System.out.println("All tasks:");
        manager.printTaskMap();
        System.out.println("---------------------");

        subTask2.setStatus("IN_PROGRESS");
        manager.updateTask(subTask2);
        System.out.println("Now subTask2 and Epic1 update IN_PROGRESS");
        manager.printTaskMap();
        System.out.println("---------------------");

        System.out.println("Get all from type:");
        System.out.println(manager.getAllFromType("task"));
        System.out.println(manager.getAllFromType("subtask"));
        System.out.println(manager.getAllFromType("epic"));
        System.out.println("invalid type");
        System.out.println(manager.getAllFromType("asdfafaf"));
        System.out.println("Get by ID");
        System.out.println(manager.getTaskByID(1));
        System.out.println(manager.getAllSubTaskOfEpicByEpicID(2));
        System.out.println("---------------------");


        manager.deleteTaskByID(1);
        manager.printTaskMap();
        System.out.println("---------------------");

        System.out.println("invalid type");
        manager.deleteAllFromType("asdcac");
        System.out.println("Delete from type");
        manager.deleteAllFromType("subtask");
        System.out.println("Delete all subtasks from current epic");
        manager.deleteAllSubTasksFromCurrentEpic(5);
        manager.printTaskMap();
        System.out.println("---------------------");

        System.out.println("Delete all epics");
        manager.deleteAllFromType("epic");
        manager.printTaskMap();
        System.out.println("---------------------");

        System.out.println("Delete all tasks");
        manager.deleteAllFromType("task");
        manager.printTaskMap();
        System.out.println("---------------------");
    }
}
