package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.Main;
import com.qut.cab302_project_pomodora.config.Theme;
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
import java.util.Objects;

public class NavbarController {

    @FXML private AnchorPane navBarPane;
    @FXML private Button studyButton;
    @FXML private VBox navButtonList;

    @FXML
    private void toggleNavbar(ActionEvent event) {
        System.out.println("toggleNavbar");
    }

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

    public void setNavButtonStyles(Scene scene) {
        System.out.println("setNavButtonStyles" + navButtonList);
        System.out.println(navButtonList.getChildren());
        for (Node button : navButtonList.getChildren()) {
            if (button.getClass() == Button.class) {
                System.out.println(button.getId());
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
