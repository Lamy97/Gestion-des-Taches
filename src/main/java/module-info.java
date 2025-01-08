module org.example.gestion_de_taches {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.gestion_de_taches.Controller to javafx.fxml;
    opens org.example.gestion_de_taches.model to javafx.base;
    exports org.example.gestion_de_taches;
}