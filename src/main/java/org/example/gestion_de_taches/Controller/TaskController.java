package org.example.gestion_de_taches.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.gestion_de_taches.model.Task;
import org.example.gestion_de_taches.model.TaskRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.stream.Collectors;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TaskController {

    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> titleColumn;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TableColumn<Task, String> priorityColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;
    @FXML
    private TableColumn<Task, String> categoryColumn;
    @FXML
    private TableColumn<Task, LocalDate> dueDateColumn;
    @FXML
    private ComboBox<String> filterPriority;
    @FXML
    private ComboBox<String> filterStatus;
    @FXML
    private ComboBox<String> filterCategory;
    @FXML
    private TextField searchField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private ComboBox<String> priorityField;
    @FXML
    private ComboBox<String> statusField;
    @FXML
    private ComboBox<String> categoryField;
    @FXML
    private DatePicker dueDateField;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button exportButton;
    @FXML
    private Label statsLabel;

    private TaskRepository repository = new TaskRepository();

    @FXML
    public void initialize() {
        // Initialize columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        // Add sample data
        repository.addTask(new Task("Presentation", "Prepare A Presentation with prezi", "Haute", "À faire", "Personnel", LocalDate.now().plusDays(1)));
        repository.addTask(new Task("Finish My Project", " Add some style & Test it", "Moyenne", "En cours", "Travail", LocalDate.now().plusDays(2)));

        taskTable.setItems(repository.getTasks());

        // Populate filter options
        filterPriority.setItems(FXCollections.observableArrayList("Toutes", "Haute", "Moyenne", "Basse"));
        filterStatus.setItems(FXCollections.observableArrayList("Tous", "À faire", "En cours", "Terminé"));
        filterCategory.setItems(FXCollections.observableArrayList("Toutes", "Travail", "Personnel", "Urgent"));
        filterPriority.setValue("Toutes");
        filterStatus.setValue("Tous");
        filterCategory.setValue("Toutes");

        filterPriority.setOnAction(event -> filterTasks());
        filterStatus.setOnAction(event -> filterTasks());
        filterCategory.setOnAction(event -> filterTasks());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterTasks());

        // Populate priority, status, and category options
        priorityField.setItems(FXCollections.observableArrayList("Haute", "Moyenne", "Basse"));
        statusField.setItems(FXCollections.observableArrayList("À faire", "En cours", "Terminé"));
        categoryField.setItems(FXCollections.observableArrayList("Travail", "Personnel", "Urgent"));

        // Set button actions
        addButton.setOnAction(event -> addTask());
        updateButton.setOnAction(event -> updateTask());
        deleteButton.setOnAction(event -> deleteTask());
        exportButton.setOnAction(event -> exportTasksToCSV());
        // Add listener to the taskTable selection model
        taskTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showTaskDetails(newValue));
        updateStats();
    }

    private void filterTasks() {
        String priority = filterPriority.getValue();
        String status = filterStatus.getValue();
        String category = filterCategory.getValue();
        String searchText = searchField.getText().toLowerCase();

        ObservableList<Task> filteredTasks = repository.getTasks().stream()
                .filter(task -> (priority.equals("Toutes") || task.getPriority().equals(priority)) &&
                        (status.equals("Tous") || task.getStatus().equals(status)) &&
                        (category.equals("Toutes") || task.getCategory().equals(category)) &&
                        (task.getTitle().toLowerCase().contains(searchText) || task.getDescription().toLowerCase().contains(searchText) || task.getCategory().toLowerCase().contains(searchText)))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        taskTable.setItems(filteredTasks);
        updateStats();
    }

    private void addTask() {
        Task newTask = new Task(titleField.getText(), descriptionField.getText(), priorityField.getValue(), statusField.getValue(), categoryField.getValue(), dueDateField.getValue());
        repository.addTask(newTask);
        taskTable.setItems(repository.getTasks());
        updateStats();
    }

    private void updateTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            Task updatedTask = new Task(titleField.getText(), descriptionField.getText(), priorityField.getValue(), statusField.getValue(), categoryField.getValue(), dueDateField.getValue());
            repository.updateTask(selectedTask, updatedTask);
            taskTable.setItems(repository.getTasks());
            updateStats();
        }
    }

    private void deleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            repository.removeTask(selectedTask);
            taskTable.setItems(repository.getTasks());
            updateStats();
        }
    }

    private void updateStats() {
        long totalTasks = repository.getTasks().size();
        long todoTasks = repository.getTasks().stream().filter(task -> task.getStatus().equals("À faire")).count();
        long inProgressTasks = repository.getTasks().stream().filter(task -> task.getStatus().equals("En cours")).count();
        long doneTasks = repository.getTasks().stream().filter(task -> task.getStatus().equals("Terminé")).count();

        statsLabel.setText(String.format("Total: %d, À faire: %d, En cours: %d, Terminé: %d", totalTasks, todoTasks, inProgressTasks, doneTasks));
    }

    private void exportTasksToCSV() {
        List<Task> tasksToExport = taskTable.getItems(); // Get the currently displayed tasks
        try (FileWriter writer = new FileWriter("tasks_export.csv")) {
            writer.append("Title,Description,Priority,Status,Category,DueDate\n");
            for (Task task : tasksToExport) {
                writer.append(task.getTitle()).append(',')
                        .append(task.getDescription()).append(',')
                        .append(task.getPriority()).append(',')
                        .append(task.getStatus()).append(',')
                        .append(task.getCategory()).append(',')
                        .append(task.getDueDate().toString()).append('\n');
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showTaskDetails(Task task) {
        if (task != null) {
            // Fill the input fields with the task details
            titleField.setText(task.getTitle());
            descriptionField.setText(task.getDescription());
            priorityField.setValue(task.getPriority());
            statusField.setValue(task.getStatus());
            categoryField.setValue(task.getCategory());
            dueDateField.setValue(task.getDueDate());
        } else {
            // Clear the input fields if no task is selected
            titleField.clear();
            descriptionField.clear();
            priorityField.setValue(null);
            statusField.setValue(null);
            categoryField.setValue(null);
            dueDateField.setValue(null);
        }
    }
}