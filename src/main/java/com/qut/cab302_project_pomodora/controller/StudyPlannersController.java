package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StudyPlannersController is responsible for managing the study planners view in the application.
 * It handles the display of active and past study plans, as well as the creation of new study plans.
 */
public class StudyPlannersController extends ControllerSkeleton {

    @FXML private Region navbar;
    @FXML private NavbarController navbarController;

    @FXML private StackPane studyPlanners;
    @FXML private StackPane contentPane;
    @FXML private GridPane activeStudyPlansGrid;
    @FXML private GridPane pastStudyPlansGrid;
    @FXML private ScrollPane scrollPane;

    @FXML private StackPane newStudyPlanPopUp;
    @FXML private StackPane studyPlanDetailsPopUp;

    private static final int MAX_COLUMNS = 3;
    private static final double PREF_VBOX_HEIGHT = 340;
    private static final double PREF_VBOX_WIDTH = 517;

    // Mock Data Structure
//    private record StudyPlan(String id, String title, boolean isActive, int tasksRemaining) {}
//
//    private List<StudyPlan> mockStudyPlans;


    // DAO interfaces
    private IStudyPlanDAO studyPlanDAO;
    private IUserDAO userDAO;
    private ITaskDAO taskDAO;

    // Current user object
    private User currentUser;

    // Study plans list
    private List<StudyPlan> studyPlans;

    public StudyPlannersController(){
        // Initialize the DAO interfaces
        studyPlanDAO = new SqliteStudyPlanDAO();
        userDAO = new SqliteUserDAO();
        taskDAO = new SqliteTaskDAO();
    }

    /**
     * Gets the root pane of the study planners view.
     * @return the root pane of the study planners view.
     */
    @Override
    protected StackPane getRootPane() {
        return studyPlanners;
    }

    /**
     * Gets the navbar of the study planners view.
     * @return the navbar of the study planners view.
     */
    @Override
    protected Region getContentPane() {
        return contentPane;
    }

    /**
     * Initializes the study planners view.
     * @throws SQLException if there is an error with the database connection.
     * @throws IOException if there is an error with the FXML file.
     */
    @Override
    @FXML
    public void initialize() throws SQLException, IOException {
        super.initialize();
        currentUser = SessionManager.getCurrentUser();


        if(currentUser == null) {
            throw new IllegalStateException("Current user is null. Cannot load study plans.");
        }

        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        contentPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        studyPlans = studyPlanDAO.getAllStudyPlans(currentUser.getId());

//        // Mock Study Plan and Tasks for view details pop up testing
//        StudyPlan mock = new StudyPlan(currentUser.getId(),"Mock Study Plan", "This is a test plan", "ACTIVE");
//        List<Task> mockTasks = new ArrayList<>();
//        mockTasks.add(new Task(101,1,"Mock Task 1", "This is a mock task description", "PENDING"));
//        mockTasks.add(new Task(101, 2, "Mock Task 2", "This is another mock task description", "IN_PROGRESS"));
//        mock.setTasks(mockTasks);
//
//        studyPlans.add(mock);

        //-----------
        populateStudyPlanGrids();
        Platform.runLater(() -> {
                    navbarController.setNavButtonStyles(studyPlanners.getScene());

                    // Need this here for preventing side scrolling entirely.
                    scrollPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                        if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                            event.consume();
                        }
                    });
                });


        System.out.println("StudyPlannersController Initialization completed.");
    }

//    private void createMockData() {
//        mockStudyPlans = new ArrayList<>();
//        mockStudyPlans.add(new StudyPlan("plan-cs-proj", "CS Project", true, 3));
//        mockStudyPlans.add(new StudyPlan("plan-theo-essay", "Theology Essay", true, 5));
//        mockStudyPlans.add(new StudyPlan("plan-math-hw", "Math Homework", true, 1));
//        mockStudyPlans.add(new StudyPlan("plan-chem-lab", "Chemistry Lab", true, 8));
//        mockStudyPlans.add(new StudyPlan("plan-cs-exam-new", "CS Exam 2", true, 100));// Example for wrapping
//        mockStudyPlans.add(new StudyPlan("plan-cs-exam", "CS Exam", false, 0));
//        mockStudyPlans.add(new StudyPlan("plan-bio-report", "Biology Report", false, 0));
//        mockStudyPlans.add(new StudyPlan("plan-phys-test", "Physics Data Test", false, 0));
//        mockStudyPlans.add(new StudyPlan("plan-hist-paper", "History Paper", false, 0)); // Example for wrapping past
//    }

    /**
     * Populates the study plan grids with active and past study plans.
     * This method separates the plans into active and past categories and populates the respective grid panes.
     */
    private void populateStudyPlanGrids() {
        //TODO: Create database table for studyplans and connect here. We don't need to keep the current data mockup, though it would prob be easiest to

        // Separate plans into active and past (uses list filtering)
        List<StudyPlan> activePlans = studyPlans.stream()
                .filter(StudyPlan::isActive)
                .collect(Collectors.toList());

        List<StudyPlan> pastPlans = studyPlans.stream()
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
            VBox noPastPlans = createStudyPlanVBox(new StudyPlan(currentUser.getId(), "No past plans", "The user does not have any past plans", "INACTIVE"));
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
        Label titleLabel = new Label(plan.getTitle());
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
            countLabel.setText(String.valueOf(taskDAO.tasksRemaining(plan.getId())));
        } else {
            statusLabel.setText("Tasks Complete!");
            countLabel.setText(":)");
        }

        stackPane.getChildren().addAll(circle, countLabel);
        vbox.getChildren().addAll(titleLabel, statusLabel, stackPane);


        // store the plan ID
        vbox.setUserData(plan.getId());

        // on mouse clicked
        vbox.setOnMouseClicked(this::goToStudyPlan);

        return vbox;

        //TODO: Properly add styleclasses
    }


    /**
     * Opens a pop-up window.
     * @param popUp The StackPane representing the pop-up to be opened.
     */
    private void openPopUp(StackPane popUp) {popUp.setVisible(true);}
    /**
     * Closes a pop-up window.
     * @param popUp The StackPane representing the pop-up to be closed.
     */
    private void closePopUp(StackPane popUp) {popUp.setVisible(false);}

    @FXML private Button resumeStudyPlanButton;
    @FXML private Button closeStudyPlanDetailsPopUpButton;

    /**
     * Handles the action when a study plan is clicked.
     * This method opens the study plan details pop-up and loads the tasks for the selected study plan.
     * @param event The mouse event that triggered this action.
     */
    @FXML
    private void goToStudyPlan(MouseEvent event) {
        Object source = event.getSource();
        String planIdStr = "N/A";

        // Check if the source is a VBox and get the user data
        if (source instanceof Node node) {
            // Get the user data from the clicked node and parse it to an integer
            Object userData = node.getUserData();
            int selectedStudyPlanId = Integer.parseInt(String.valueOf(userData));

            // Find the selected study plan from the list
            StudyPlan selectedStudyPlan = studyPlans.stream()
                    .filter(plan -> plan.getId() == selectedStudyPlanId)
                    .findFirst()
                    .orElse(null);

            // If the selected study plan is found, display its details
            if (selectedStudyPlan != null) {
                System.out.println("Selected Study Plan: " + selectedStudyPlan.getTitle());
                List<Task> tasks = taskDAO.getTasksByStudyPlan(selectedStudyPlanId);
                displayTasks(tasks);
                openPopUp(studyPlanDetailsPopUp);
            }

            // I started extending bits from here  -Sriman
//            if (userData instanceof String planIDStr){
//                if (planIDStr.equals("N/A")){
//                    System.err.println("Invalid planID format: N/A");
//                    return;
//                }
//                try {
//                    int planID = Integer.parseInt(planIdStr);
//
//                    StudyPlan selectedPlan = studyPlans.stream()
//                            .filter(plan -> plan.getId() == planID).findFirst().orElse(null);
//
//                    if (selectedPlan != null) {
//                        System.out.println("Selected Study Plan: " + selectedPlan.getTitle());
//                        List<Task> tasks = taskDAO.getTasksByStudyPlan(selectedPlan.getId());
//                        displayTasks(tasks);
//                        openPopUp(studyPlanDetailsPopUp);
//                    } else {
//                        System.err.println("No study plan found with id: " + planID);
//                    }
//                } catch (NumberFormatException e) {
//                    System.err.println("PlanID is not a valid number: " + planIDStr);
//                }
//            }
        } else {
                System.err.println("UserData is not a Studyplan object or is null.");
            }
    }



    // Box within the studyPlanDetails pop-up that will actually show the tasks
    @FXML private VBox taskListVBox;


    // This is the method I'm trying to call    -Sriman
    @FXML
    private void displayTasks(List<Task> tasks) {
        taskListVBox.getChildren().clear();

        for (Task task : tasks) {
            HBox taskBox = new HBox();
            taskBox.setSpacing(10);
            taskBox.setAlignment(Pos.TOP_LEFT);
            taskBox.setPadding(new Insets(10));
            taskBox.setStyle("-fx-border-color: #ccc; -fx-border-width: 0 0 1 0;");

            VBox textBox = new VBox();
            textBox.setAlignment(Pos.TOP_LEFT);

            Label titleLabel = new Label(task.getTitle());
            titleLabel.setStyle("-fx-font-weight: bold;");

            Label descLabel = new Label(task.getDescription());
            descLabel.setWrapText(true);

            textBox.getChildren().addAll(titleLabel, descLabel);
            HBox.setHgrow(textBox, Priority.ALWAYS);

            CheckBox checkBox = new CheckBox();
            checkBox.setAlignment(Pos.TOP_RIGHT);

            taskBox.getChildren().addAll(textBox, checkBox);
            taskListVBox.getChildren().add(taskBox);

        }
    }

    /**
     * Handles the action when the "Resume Study Plan" button is clicked.
     * This method should navigate to the specific study plan's timer page.
     */
    @FXML
    private void resumeStudyPlan() {
        // TODO: Go to specific study plan's timer page
    }

    /**
     * Closes the study plan details pop-up.
     */
    @FXML
    private void closeStudyPlanDetailsPopUp(){
        closePopUp(studyPlanDetailsPopUp);
    }

    @FXML private TextArea promptEntryTextArea;
    @FXML private TextField studyHoursEntryTextField;
    @FXML private Button generateStudyPlanButton;
    @FXML private Button closeCreateStudyPlanPopUpButton;

    /**
     * Handles the action when the "Create New Study Plan" button is clicked.
     * This method opens the pop-up for creating a new study plan.
     * @param event The action event that triggered this method.
     */
    @FXML
    private void createNewStudyPlan(ActionEvent event) {
        System.out.println("createNewStudyPlan button clicked");
        openPopUp(newStudyPlanPopUp);
    }

    /**
     * Handles the action when the "Generate Study Plan" button is clicked.
     * This method should send the input data to the API for generating a study plan.
     */
    @FXML
    private void generateStudyPlan(){
        /* TODO: take input from 'promptEntryTextArea' and 'studyHoursEntryTextField
            to send to API */
    }

    /**
     * Handles the action when the "Close" button is clicked in the create study plan pop-up.
     * This method closes the pop-up and resets the input fields.
     */
    @FXML
    private void closeCreateStudyPlanPopUp(){
        closePopUp(newStudyPlanPopUp);
    }
}