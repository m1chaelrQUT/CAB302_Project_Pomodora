module com.example.cab302_project_pomodora {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.desktop;


    opens com.qut.cab302_project_pomodora to javafx.fxml;
    exports com.qut.cab302_project_pomodora;
    exports com.qut.cab302_project_pomodora.controller;
    opens com.qut.cab302_project_pomodora.controller to javafx.fxml;
}