package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.Main;
import com.qut.cab302_project_pomodora.config.Theme;
import com.qut.cab302_project_pomodora.model.SessionManager;
import com.qut.cab302_project_pomodora.util.ThemeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;


import java.util.Objects;

/**
 * NavbarController is responsible for managing the navigation bar in the application.
 * It handles navigation between different scenes and manages the visibility of the navbar.
 */
public class NavbarController {

    // FXML elements
    @FXML private AnchorPane navBarPane;
    @FXML private Button studyButton;
    @FXML private VBox navButtonList;

    /**
     * Toggles the visibility of the navbar.
     *@param event The event that triggered the initialization.
     */
    @FXML
    private void toggleNavbar(ActionEvent event) {
        System.out.println("toggleNavbar");
        //TODO: Implement toggleNavbar logic
    }


    /**
     * Navigates to the specified scene when a button is clicked.
     * @param event The event that triggered the navigation.
     */
    @FXML
    private void navigateToAction(ActionEvent event) throws IOException {

        // ALL TEMP DEBUGGING LOGIC
        Object buttonClicked = event.getSource();
        String buttonID = "N/A";
        if (buttonClicked instanceof Button) {
            buttonID = (String) ((Button) buttonClicked).getId();
        }
        System.out.println("navigateTo: " + buttonID + ", From: " + navBarPane.getScene().getRoot().getId());
        navigateTo(buttonID);
    }

    /**
     * Navigates to the specified scene.
     * @param toSceneName The name of the scene to navigate to.
     */
    private void navigateTo(String toSceneName) throws IOException {
        Scene currentScene = navBarPane.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        String toScenePath = "/com/qut/cab302_project_pomodora/fxml/pages/" + toSceneName + ".fxml";
        System.out.println("Navigating to: " + toScenePath);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(toScenePath));
        Scene scene = new Scene(fxmlLoader.load(), currentScene.getWidth(), currentScene.getHeight());
        stage.setScene(scene);
    }

    /**
     * Logs out the user and ends the session.
     * @param event The event that triggered the logout.
     */
    @FXML
    private void logOut(ActionEvent event) throws IOException, SQLException {
        // Log out the user and end the session
        System.out.println("Logging out user: " + SessionManager.getCurrentUser().getUserName() + ", closing session...");
        SessionManager.endSession();
        navigateTo("signin");
    }

    /**
     * Sets the theme of the navbar.
     * @param scene The current scene.
     */
    public void setNavButtonStyles(Scene scene) {
        System.out.println("setNavButtonStyles");
        for (Node button : navButtonList.getChildren()) {
            if (button.getClass() == Button.class) {
                if (Objects.equals(((Button) button).getId(), scene.getRoot().getId())) {
                    button.getStyleClass().clear();
                    button.getStyleClass().add("current-page-nav-button");
                } else {
                    button.getStyleClass().clear();
                    button.getStyleClass().add("button-primary");
                }
            }
        }
    }
}
