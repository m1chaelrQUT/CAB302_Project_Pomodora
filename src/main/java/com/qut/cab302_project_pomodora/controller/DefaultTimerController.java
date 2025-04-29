package com.qut.cab302_project_pomodora.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class DefaultTimerController extends ControllerSkeleton{

    @FXML private StackPane defaultTimer;
    @FXML private Region contentPane;

    @FXML private Region navbar;
    @FXML private NavbarController navbarController;

    @Override
    protected StackPane getRootPane() {
        return defaultTimer;
    }

    @Override
    protected Region getContentPane() {
        return contentPane;
    }

    @Override
    public void initialize() {
        super.initialize();

        Platform.runLater(() -> {
            navbarController.setNavButtonStyles(defaultTimer.getScene());
        });


    }
}
