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

    /**
     * Initializes the session by loading the current user from the session manager.
     * This method is called during the initialization of the controller.
     *
     * @throws SQLException if there is an error loading the session from the database
     * @throws IOException  if there is an error loading the session from the file
     */
    public void iniSession() throws SQLException, IOException {
        // Load the session to check if the user is already logged in
        SessionManager.loadSession();

        User currentUser = SessionManager.getCurrentUser();
        System.out.println("Session loaded!");
    }



}
