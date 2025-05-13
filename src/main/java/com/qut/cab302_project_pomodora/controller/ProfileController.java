package com.qut.cab302_project_pomodora.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.SQLException;

public class ProfileController extends ControllerSkeleton{

    @FXML private StackPane profile;
    @FXML private Region contentPane;

    @FXML private Region navbar;
    @FXML private NavbarController navbarController;

    @FXML private BarChart<String, Number> tasksDoneBarChart;
    @FXML private ProgressBar levelProgressIndicator;

    @Override
    protected StackPane getRootPane() {
        return profile;
    }

    @Override
    protected Region getContentPane() {
        return contentPane;
    }

    @Override
    public void initialize() throws SQLException, IOException {
        super.initialize();

        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        contentPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        XYChart.Series<String, Number> tasksDoneSeries = new XYChart.Series<>();
        tasksDoneSeries.getData().add(new XYChart.Data<>("1", 0));
        tasksDoneSeries.getData().add(new XYChart.Data<>("2", 3));
        tasksDoneSeries.getData().add(new XYChart.Data<>("3", 5));
        tasksDoneSeries.getData().add(new XYChart.Data<>("4", 2));
        tasksDoneSeries.getData().add(new XYChart.Data<>("5", 1));
        tasksDoneSeries.getData().add(new XYChart.Data<>("6", 0));
        tasksDoneSeries.getData().add(new XYChart.Data<>("7", 3));
        tasksDoneSeries.getData().add(new XYChart.Data<>("7", 3));
        tasksDoneBarChart.getData().add(tasksDoneSeries);

        levelProgressIndicator.setProgress(0.5);


        Platform.runLater(() -> {
            navbarController.setNavButtonStyles(profile.getScene());
        });
    }
}
