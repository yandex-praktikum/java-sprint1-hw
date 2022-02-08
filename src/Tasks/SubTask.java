package Tasks;

public class SubTask extends Task {

    private int epicID;

    public SubTask(String name, String description, String status, int epicID) {
        super(name, description, status);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    @Override
    public String getTaskType() {
        return "subtask";
    }

    @Override
    public String toString() {
        return "Tasks.SubTask{" +
                "epicID=" + epicID +
                "} " + super.toString();
    }
}
