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

/**
 * ProfileController is responsible for managing the profile view in the application.
 * It handles the display of user statistics and progress indicators.
 */
public class ProfileController extends ControllerSkeleton{

    @FXML private StackPane profile;
    @FXML private Region contentPane;

    @FXML private Region navbar;
    @FXML private NavbarController navbarController;

    @FXML private BarChart<String, Number> tasksDoneBarChart;
    @FXML private ProgressBar levelProgressIndicator;

    /**
     * gets the root pane of the profile view.
     * @return the root pane of the profile view.
     */
    @Override
    protected StackPane getRootPane() {
        return profile;
    }

    /**
     * gets the navbar of the profile view.
     * @return the navbar of the profile view.
     */
    @Override
    protected Region getContentPane() {
        return contentPane;
    }

    /**
     * initializes the profile view.
     * @throws SQLException throws SQLException if there is an error with the database connection.
     * @throws IOException throws IOException if there is an error with the FXML file.
     */
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
