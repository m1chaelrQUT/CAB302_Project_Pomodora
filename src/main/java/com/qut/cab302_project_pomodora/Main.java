package com.qut.cab302_project_pomodora;

import com.qut.cab302_project_pomodora.model.SessionManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.qut.cab302_project_pomodora.config.Theme;
import com.qut.cab302_project_pomodora.util.ThemeManager;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        int START_WIDTH = 1920;
        int START_HEIGHT = 1080;
        int MIN_WIDTH = 1067;
        int MIN_HEIGHT = 600;
        String TITLE = "CAB302: Project Pomodora";
        String LAUNCH_PAGE_ADDRESS = "fxml/pages/signin.fxml";
        String MAIN_PAGE_ADDRESS = "fxml/pages/studyplanners.fxml";
        String APP_ICON_ADDRESS = "images/logocolor.png";

        // Load session to check if the user is already logged in
        try {
            SessionManager.loadSession();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        // Create a new FXMLLoader instance
        FXMLLoader fxmlLoader;

        // Check if the user is already logged in
        if (SessionManager.getCurrentUser() != null) {
            // Load the home page if the user is logged in
            fxmlLoader = new FXMLLoader(Main.class.getResource(MAIN_PAGE_ADDRESS));
        } else {
            // Load the sign-in page if the user is not logged in
            fxmlLoader = new FXMLLoader(Main.class.getResource(LAUNCH_PAGE_ADDRESS));
        }

        // Load the FXML file and set the scene
        Scene scene = new Scene(fxmlLoader.load(), START_WIDTH, START_HEIGHT);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream(APP_ICON_ADDRESS)));
        ThemeManager.getInstance().applyTheme(scene, Theme.LIGHT);
        stage.setResizable(true);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setMinWidth(MIN_WIDTH);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}