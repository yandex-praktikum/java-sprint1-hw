public class Main {
    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        Task task = new Task("Task", "desc", "IN_PROGRESS");
        manager.createTask(task);

        Task task1 = new Task("Task1", "desc", "IN_PROGRESS");
        manager.createTask(task1);

        Epic epic = new Epic("epic1", "description", "NEW");
        manager.createTask(epic);

        SubTask subTask1 = new SubTask("sub1", "des1", "NEW", epic.getId());
        manager.createTask(subTask1);

        SubTask subTask2 = new SubTask("sub", "des2", "NEW", epic.getId());
        manager.createTask(subTask2);

        Epic epic2 = new Epic("epic2", "description", "NEW");
        manager.createTask(epic2);

        SubTask subTask = new SubTask("sub", "des", "NEW", epic2.getId());
        manager.createTask(subTask);
        System.out.println(manager.getAllTask());
        System.out.println("---------------------");

        subTask2.setStatus("IN_PROGRESS");
        manager.updateTask(subTask2);
        System.out.println(manager.getAllTask());
        System.out.println("---------------------");

        System.out.println(manager.getAllFromType("task"));
        System.out.println(manager.getAllFromType("subtask"));
        System.out.println(manager.getAllFromType("epic"));
        System.out.println(manager.getAllFromType("asdfafaf"));
        System.out.println(manager.getTaskByID(1));
        System.out.println(manager.getAllSubTaskOfEpicByEpicID(2));
        System.out.println("---------------------");


        manager.deleteTaskByID(1);
        System.out.println(manager.getAllTask());
        System.out.println("---------------------");

        manager.deleteAllFromType("asdcac");
        manager.deleteAllFromType("subtask");
        manager.deleteAllSubTasksFromCurrentEpic(5);
        System.out.println(manager.getAllTask());
        System.out.println("---------------------");

        manager.deleteAllFromType("epic");
        System.out.println(manager.getAllTask());
        System.out.println("---------------------");

        manager.deleteAllFromType("task");
        System.out.println(manager.getAllTask());
        System.out.println("---------------------");
    }
}
