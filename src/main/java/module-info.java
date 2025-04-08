module com.example.cab302_project_pomodora {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cab302_project_pomodora to javafx.fxml;
    exports com.example.cab302_project_pomodora;
}