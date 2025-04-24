package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.Main;
import com.qut.cab302_project_pomodora.config.Theme;
import com.qut.cab302_project_pomodora.util.ThemeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class NavbarController {

    @FXML private AnchorPane navBarPane;
    @FXML private Button studyButton;

    @FXML
    private void toggleNavbar(ActionEvent event) {
        System.out.println("toggleNavbar");
    }

    @FXML
    private void navigateToAction(ActionEvent event) {

        // ALL TEMP DEBUGGING LOGIC
        System.out.println("navigateTo event: " + event + ", From: " + event.getSource() + ", Current Scene: " + navBarPane.getScene().getRoot().getId());
        ThemeManager themeManager = ThemeManager.getInstance();
        if (themeManager.getCurrentTheme() == Theme.DARK) {
            themeManager.applyTheme(navBarPane.getScene(), Theme.VOIDLIGHT);
        } else if (themeManager.getCurrentTheme() == Theme.LIGHT) {
            themeManager.applyTheme(navBarPane.getScene(), Theme.DARK);
        } else if (themeManager.getCurrentTheme() == Theme.VOIDLIGHT) {
            themeManager.applyTheme(navBarPane.getScene(), Theme.LIGHT);
        } else {
            themeManager.applyTheme(navBarPane.getScene(), Theme.DARK);
        }
    }

    private void navigateTo(String toSceneName) throws IOException {
        Scene currentScene = navBarPane.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        String toScenePath = "/com/qut/cab302_project_pomodora/fxml/pages/" + toSceneName + ".fxml";
        System.out.println("Navigating to: " + toScenePath);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(toScenePath));
        Scene scene = new Scene(fxmlLoader.load(), currentScene.getWidth(), currentScene.getHeight());
        stage.setScene(scene);
    }

    @FXML
    private void logOut(ActionEvent event) throws IOException {
        System.out.println("logOut");
        navigateTo("signin");
    }
}
