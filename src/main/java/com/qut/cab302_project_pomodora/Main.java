package com.qut.cab302_project_pomodora;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        int START_WIDTH = 1920;
        int START_HEIGHT = 1080;
        int MIN_WIDTH = 1067;
        int MIN_HEIGHT = 600;
        String TITLE = "CAB302: Project Pomodora";
        String LAUNCH_PAGE_ADDRESS = "fxml/home.fxml";

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(LAUNCH_PAGE_ADDRESS));
        Scene scene = new Scene(fxmlLoader.load(), START_WIDTH, START_HEIGHT);
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