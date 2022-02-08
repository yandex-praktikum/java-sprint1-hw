package Tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTaskIDs = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, "NEW");
    }

    public ArrayList<Integer> getSubTaskIDs() {
        return subTaskIDs;
    }

    public void setSubTaskIDs(ArrayList<Integer> subTaskIDs) {
        this.subTaskIDs = subTaskIDs;
    }

    @Override
    public String getTaskType() {
        return "epic";
    }

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }

    @Override
    public String toString() {
        return "Tasks.Epic{" +
                "subTaskIDs=" + subTaskIDs +
                "} " + super.toString();
    }
}
