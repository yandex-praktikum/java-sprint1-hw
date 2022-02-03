package serviceclasses;

import java.util.Objects;

import enums.Status;

public class Task {

    private static int nextId = 1;

    private final int id;
    private String description;
    private Status status;
    private String title;

    public Task(String title, String description) {
        this.id = nextId;
        this.description = description;
        this.status = Status.NEW;
        this.title = title;
        nextId++;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Task{" +
                "status=" + status +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id
                && Objects.equals(description, task.description)
                && status == task.status
                && Objects.equals(title, task.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, status, title);
    }
}
