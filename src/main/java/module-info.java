module com.example.cab302_project_pomodora {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens com.qut.cab302_project_pomodora to javafx.fxml;
    exports com.qut.cab302_project_pomodora;
    exports com.qut.cab302_project_pomodora.controller;
    opens com.qut.cab302_project_pomodora.controller to javafx.fxml;
    exports com.qut.cab302_project_pomodora.model;
    opens com.qut.cab302_project_pomodora.model to javafx.fxml;
}