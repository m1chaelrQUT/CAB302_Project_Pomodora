package com.qut.cab302_project_pomodora.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.SQLException;

public class ProfileController extends ControllerSkeleton{

    @FXML private StackPane profile;
    @FXML private Region contentPane;

    @FXML private Region navbar;
    @FXML private NavbarController navbarController;

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

        Platform.runLater(() -> {
            navbarController.setNavButtonStyles(profile.getScene());
        });
    }
}
