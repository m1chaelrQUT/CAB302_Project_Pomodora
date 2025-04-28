package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.model.SessionManager;
import com.qut.cab302_project_pomodora.model.User;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.SQLException;

public class HomeExampleController extends  ControllerSkeleton {

    @FXML private StackPane homeExample;
    @FXML private AnchorPane contentPane;

    @Override
    protected StackPane getRootPane() {
        return homeExample;
    }

    @Override
    protected Region getContentPane() {
        return contentPane;
    }

    // Init
    @Override
    @FXML
    public void initialize() throws SQLException, IOException {
        super.initialize();

        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        contentPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        System.out.println("HomeExampleController Initialization completed.");

        iniSession();

    }

    public void iniSession() throws SQLException, IOException {
        SessionManager.loadSession();

        User currentUser = SessionManager.getCurrentUser();
        System.out.println("Session loaded!");
    }



}
