package com.qut.cab302_project_pomodora.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class StudyPlannersController extends  ControllerSkeleton {

    @FXML private StackPane homeExample;
    @FXML private BorderPane contentPane;

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
    public void initialize() {
        super.initialize();

        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        contentPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        System.out.println("SigninController Initialization completed.");
    }

    @FXML private void goToStudyPlan(MouseEvent event) {
        Object source = event.getSource();
        String fxId;
        if (source instanceof Node node) {
            fxId = node.getId();
        } else {fxId = "N/A";}
        System.out.println("goToStudyPlan: " + fxId);
    }

}
