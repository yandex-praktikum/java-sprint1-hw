package tasks;

import enums.Status;
import enums.TypeOfTask;

public class SubTask extends Task {

    private final int epicID;

    public SubTask(String name, String description, Status status, int epicID) {
        super(name, description, status);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public TypeOfTask getTaskType() {
        return TypeOfTask.SUBTASK;
    }

    @Override
    public String toString() {
        return "Tasks.SubTask{" +
                "epicID=" + epicID +
                "} " + super.toString();
    }
}
