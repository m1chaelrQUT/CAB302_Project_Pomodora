package com.qut.cab302_project_pomodora.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudyPlannersController extends ControllerSkeleton {

    @FXML private Region navbar;
    @FXML private NavbarController navbarController;

    @FXML private StackPane studyPlanners;
    @FXML private BorderPane contentPane;
    @FXML private GridPane activeStudyPlansGrid;
    @FXML private GridPane pastStudyPlansGrid;

    private static final int MAX_COLUMNS = 3;
    private static final double PREF_VBOX_HEIGHT = 350.0;
    private static final double PREF_VBOX_WIDTH = 550.0;

    // Mock Data Structure
    private record StudyPlan(String id, String title, boolean isActive, int tasksRemaining) {}

    private List<StudyPlan> mockStudyPlans;

    @Override
    protected StackPane getRootPane() {
        return studyPlanners;
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

        createMockData();

        //-----------
        populateStudyPlanGrids();
        Platform.runLater(() -> {
                    navbarController.setNavButtonStyles(studyPlanners.getScene());
                });
        System.out.println("StudyPlannersController Initialization completed.");
    }

    private void createMockData() {
        mockStudyPlans = new ArrayList<>();
        mockStudyPlans.add(new StudyPlan("plan-cs-proj", "CS Project", true, 3));
        mockStudyPlans.add(new StudyPlan("plan-theo-essay", "Theology Essay", true, 5));
        mockStudyPlans.add(new StudyPlan("plan-math-hw", "Math Homework", true, 1));
        mockStudyPlans.add(new StudyPlan("plan-chem-lab", "Chemistry Lab", true, 8));
        mockStudyPlans.add(new StudyPlan("plan-cs-exam-new", "CS Exam 2", true, 100));// Example for wrapping
        mockStudyPlans.add(new StudyPlan("plan-cs-exam", "CS Exam", false, 0));
        mockStudyPlans.add(new StudyPlan("plan-bio-report", "Biology Report", false, 0));
        mockStudyPlans.add(new StudyPlan("plan-phys-test", "Physics Data Test", false, 0));
        mockStudyPlans.add(new StudyPlan("plan-hist-paper", "History Paper", false, 0)); // Example for wrapping past
    }

    private void populateStudyPlanGrids() {
        //TODO: Create database table for studyplans and connect here. We don't need to keep the current data mockup, though it would prob be easiest to

        // Separate plans into active and past (uses list filtering)
        List<StudyPlan> activePlans = mockStudyPlans.stream()
                .filter(StudyPlan::isActive)
                .collect(Collectors.toList());

        List<StudyPlan> pastPlans = mockStudyPlans.stream()
                .filter(plan -> !plan.isActive())
                .collect(Collectors.toList());

        // Populate the gridpanes
        populateGrid(activeStudyPlansGrid, activePlans, true);
        populateGrid(pastStudyPlansGrid, pastPlans, false);
    }

    /**
     * Populates a specific GridPane with study plan VBoxes
     * @param grid The GridPane to populate
     * @param plans The list of StudyPlans
     * @param isActiveGrid This is to skip the origin if we have the + button
     */
    private void populateGrid(GridPane grid, List<StudyPlan> plans, boolean isActiveGrid) {
        int col = 0;
        int row = 0;

        if (isActiveGrid) {
            // skip the + button
            col = 1;
        }

        // Check if no past plans exist, add filler if so
        if (plans.isEmpty() && !isActiveGrid) {
            VBox noPastPlans = createStudyPlanVBox(new StudyPlan("plan-past-empty", "No past plans", false, 0));
            grid.add(noPastPlans, col, row);
        }

        for (StudyPlan plan : plans) {
            // Check row wrap
            if (col >= MAX_COLUMNS) {
                col = 0;
                row++;
            }

            // Create and add plan vbox
            VBox planVBox = createStudyPlanVBox(plan);
            grid.add(planVBox, col, row);

            col++;
        }
    }


    /**
     * Creates a VBox visual representation for a StudyPlan.
     * @param plan The StudyPlan data.
     * @return A VBox Node ready to be added to the GridPane.
     */
    private VBox createStudyPlanVBox(StudyPlan plan) {
        // Container
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getStyleClass().add("existing-study-button");
        vbox.setPrefHeight(PREF_VBOX_HEIGHT);
        vbox.setPrefWidth(PREF_VBOX_WIDTH);
        vbox.setSpacing(10);

        // Title Label
        Label titleLabel = new Label(plan.title());
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setPrefHeight(85.0);
        titleLabel.setPrefWidth(504.0);
        titleLabel.setFont(new Font(40.0));

        // Status/Tasks Remaining Label
        Label statusLabel = new Label();
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setPrefHeight(74.0);
        statusLabel.setPrefWidth(504.0);
        statusLabel.setFont(new Font(30.0));

        // Task count icon container
        StackPane stackPane = new StackPane();
        stackPane.setPrefHeight(121.0);
        stackPane.setPrefWidth(544.0);

        // Circle
        Circle circle = new Circle();
        circle.setRadius(63.0);
        circle.setStroke(Color.web("#8a00b9"));
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStrokeWidth(15.0);

        // Count Label
        Label countLabel = new Label();
        countLabel.setAlignment(Pos.CENTER);
        countLabel.setPrefHeight(85.0);
        countLabel.setPrefWidth(504.0);
        countLabel.getStyleClass().add("study-plan-count-label");
        countLabel.setTextFill(Color.web("#ffffff"));
        countLabel.setFont(new Font(40.0));


        if (plan.isActive()) {
            statusLabel.setText("Tasks Remaining:");
            countLabel.setText(String.valueOf(plan.tasksRemaining()));
        } else {
            statusLabel.setText("Tasks Complete!");
            countLabel.setText(":)");
        }

        stackPane.getChildren().addAll(circle, countLabel);
        vbox.getChildren().addAll(titleLabel, statusLabel, stackPane);


        // store the plan ID
        vbox.setUserData(plan.id());

        // on mouse clicked
        vbox.setOnMouseClicked(this::goToStudyPlan);

        return vbox;

        //TODO: Properly add styleclasses
    }


    @FXML
    private void goToStudyPlan(MouseEvent event) {
        Object source = event.getSource();
        String planId = "N/A";

        if (source instanceof Node node) {
            Object userData = node.getUserData();
            if (userData instanceof String) {
                planId = (String) userData;
            }
        }
        System.out.println("goToStudyPlan triggered for plan ID: " + planId);
        // TODO: Open plan summary overlay
        // TODO: Create actual page nav method and refactor this to make sense
    }

    @FXML
    private void createNewStudyPlan(ActionEvent event) {
        System.out.println("createNewStudyPlan button clicked");
        // TODO: Open create plan overlay
    }

}