package org.example.gestion_de_taches.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskRepository {
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();

    public ObservableList<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task); // Ajout d'une tâche
    }

    public void removeTask(Task task) {
        tasks.remove(task); // Suppression d'une tâche
    }

    public void updateTask(Task oldTask, Task newTask) {
        int index = tasks.indexOf(oldTask); // Trouver l'index de l'ancienne tâche
        if (index != -1) {
            tasks.set(index, newTask); // Remplacer l'ancienne tâche par la nouvelle
        }
    }
}