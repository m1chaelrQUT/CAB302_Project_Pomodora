module com.example.cab302_project_pomodora {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires java.sql;
    requires org.json;


    opens com.qut.cab302_project_pomodora to javafx.fxml;
    exports com.qut.cab302_project_pomodora;
    exports com.qut.cab302_project_pomodora.controller;
    opens com.qut.cab302_project_pomodora.controller to javafx.fxml;
    exports com.qut.cab302_project_pomodora.model;
    opens com.qut.cab302_project_pomodora.model to javafx.fxml;
    exports com.qut.cab302_project_pomodora.util to com.fasterxml.jackson.databind;
}