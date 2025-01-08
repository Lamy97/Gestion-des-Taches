package org.example.gestion_de_taches.model;

import java.time.LocalDate;

public class Task {
    private String title;
    private String description;
    private String priority;
    private String status;
    private String category;
    private LocalDate dueDate;

    public Task(String title, String description, String priority, String status, String category, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.category = category;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}