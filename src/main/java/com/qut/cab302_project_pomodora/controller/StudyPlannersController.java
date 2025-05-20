package com.qut.cab302_project_pomodora.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qut.cab302_project_pomodora.model.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Label statusLabel;

    private LLMService llmService;

    // DAO interfaces
    private IStudyPlanDAO studyPlanDAO;
    private IUserDAO userDAO;
    private ITaskDAO taskDAO;

    // Current user object
    private User currentUser;

    private StudyPlan selectedStudyPlan;

    // Study plans list
    private List<StudyPlan> studyPlans;

    public StudyPlannersController(){
        // Initialize the DAO interfaces
        studyPlanDAO = new SqliteStudyPlanDAO();
        userDAO = new SqliteUserDAO();
        taskDAO = new SqliteTaskDAO();
    }


    private static final int MAX_COLUMNS = 3;
    private static final double PREF_VBOX_HEIGHT = 340;
    private static final double PREF_VBOX_WIDTH = 517;

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
    public void initialize() throws SQLException, IOException {
        super.initialize();
        currentUser = SessionManager.getCurrentUser();
        this.llmService = new LLMService();

        if(currentUser == null) {
            throw new IllegalStateException("Current user is null. Cannot load study plans.");
        }

        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        contentPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

//        createMockData();
        // Populate the study plans list


        studyPlans = studyPlanDAO.getAllStudyPlans(currentUser.getId());

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
                    statusLabel.textProperty().bind(llmService.statusMessageProperty());
                });

        iniSession();
        //System.out.println("StudyPlannersController" + studyPlans.size() + " StudyPlans: " + studyPlans);
        System.out.println("StudyPlannersController Initialization completed.");
    }

    /**
     * Initializes the session by loading the current user from the session manager.
     * This method is called during the initialization of the controller.
     * @throws SQLException if there is an error loading the session from the database
     * @throws IOException  if there is an error loading the session from the file
     */
    public void iniSession() throws SQLException, IOException {
        // Load the session to check if the user is already logged in
        SessionManager.loadSession();

        User currentUser = SessionManager.getCurrentUser();
        System.out.println("Session loaded!");
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

    private void populateStudyPlanGrids() {
        //TODO: Create database table for studyplans and connect here. We don't need to keep the current data mockup, though it would prob be easiest to

        // Separate plans into active and past (uses list filtering)
        List<StudyPlan> activePlans = studyPlans.stream()
                .filter(StudyPlan::planIsActive)
                .collect(Collectors.toList());

        List<StudyPlan> pastPlans = studyPlans.stream()
                .filter(plan -> !plan.planIsActive())
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
            VBox noPastPlans = createStudyPlanVBox(new StudyPlan(0, currentUser.getId(), "No past plans", "No plans for this user", "ACTIVE"));
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


        if (plan.planIsActive()) {
            statusLabel.setText("Tasks Remaining:");
//            countLabel.setText(String.valueOf(plan.tasksRemaining()));
            countLabel.setText(String.valueOf(taskDAO.tasksRemaining(plan.getId())));
        } else {
            statusLabel.setText("Tasks Complete!");
            countLabel.setText(":)");
        }

        stackPane.getChildren().addAll(circle, countLabel);
        vbox.getChildren().addAll(titleLabel, statusLabel, stackPane);


        // store the plan ID

        vbox.setUserData(plan.getTitle());

        // on mouse clicked
        vbox.setOnMouseClicked(this::goToStudyPlan);

        return vbox;

        //TODO: Properly add styleclasses
    }


    private void openPopUp(StackPane popUp) {popUp.setVisible(true);}
    private void closePopUp(StackPane popUp) {popUp.setVisible(false);}

    @FXML private Button resumeStudyPlanButton;
    @FXML private Button closeStudyPlanDetailsPopUpButton;

    private void goToStudyPlan(MouseEvent event) {
        Node source = (Node) event.getSource();
        String planTitle = (String) source.getUserData();

        selectedStudyPlan = studyPlans.stream()
                .filter(plan -> plan.getTitle().equals(planTitle))
                .findFirst()
                .orElse(null);

        if (selectedStudyPlan != null) {
            System.out.println("Selected plan: " + selectedStudyPlan.getTitle());
            openPopUp(studyPlanDetailsPopUp);
        }
    }

    @FXML
    private void resumeStudyPlan() {
        if (selectedStudyPlan == null) {
            System.out.println("No study plan selected.");
            return;
        }

        SqliteStudyPlanDAO studyPlanDAO = new SqliteStudyPlanDAO();
        boolean success = studyPlanDAO.resumeStudyPlan(currentUser.getId(), selectedStudyPlan.getId());

        if (success) {
            System.out.println("Resumed plan: " + selectedStudyPlan.getTitle());

            // Navigate to timer page
            try {
                navigateTo("studyplantimer");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Failed to resume plan.");
        }
    }


    @FXML
    private void closeStudyPlanDetailsPopUp(){
        closePopUp(studyPlanDetailsPopUp);
    }

    @FXML private TextArea promptEntryTextArea;
    @FXML private TextField studyHoursEntryTextField;
    @FXML private Button generateStudyPlanButton;
    @FXML private Button closeCreateStudyPlanPopUpButton;

    @FXML
    private void createNewStudyPlan(ActionEvent event) {
        System.out.println("createNewStudyPlan button clicked");
        openPopUp(newStudyPlanPopUp);
    }

    @FXML
    private void generateStudyPlan() throws IOException {
        /* TODO: take input from 'promptEntryTextArea' and 'studyHoursEntryTextField
            to send to API */
        System.out.println("generateStudyPlan button clicked");
        String prompt = promptEntryTextArea.getText();
        String totalHours = studyHoursEntryTextField.getText();
        handleSendPrompt(prompt, totalHours);

    }

    @FXML
    private void closeCreateStudyPlanPopUp(){
        closePopUp(newStudyPlanPopUp);
    }


    @FXML
    private void handleSendPrompt(String prompt, String hours) {
        if (prompt.isEmpty()) {
            llmService.statusMessageProperty().set("Status: Please enter a prompt.");
            return;
        }

        generateStudyPlanButton.setDisable(true);
        promptEntryTextArea.setDisable(true);
        studyHoursEntryTextField.setDisable(true);
        progressIndicator.setVisible(true);

        Task<String> ollamaTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();

                StudyPlanPartial studyPlanFormat = new StudyPlanPartial("title", "description");
                String STUDY_PLAN_FORMAT = objectMapper.writeValueAsString(studyPlanFormat);
                String systemPrompt1 = "Generate one study plan with the following format: " + STUDY_PLAN_FORMAT + ",  based on the following user prompt: " + prompt +". Keep the description concise.";

                String requestFormat = "json";
                String generatedResponse = llmService.getCompletion(systemPrompt1, requestFormat).join();

                StudyPlanPartial plan = objectMapper.readValue(generatedResponse, StudyPlanPartial.class);
                String systemPrompt2 = "Generate a list of tasks as so: {task1: (title, description), task2: (title, description), ...} for an appropriate number of tasks, based on this study plan: " + generatedResponse;
                String generatedTasks = llmService.getCompletion(systemPrompt2, requestFormat).join();


                StudyPlan generatedStudyPlan = new StudyPlan(0, currentUser.getId(), plan.getTitle(), plan.getDescription(), "ACTIVE");
                studyPlanDAO.createStudyPlan(generatedStudyPlan);

                // make map of tasks
                Map<String, StudyTaskPartial> tasks = objectMapper.readValue(
                        generatedTasks,
                        new TypeReference<Map<String, StudyTaskPartial>>() {}
                );

                // iterate over the map
                for (Map.Entry<String, StudyTaskPartial> entry : tasks.entrySet()) {
                    String key = entry.getKey();
                    String taskNumberString = key.replaceAll("\\D+", "");
                    int taskNumber = Integer.parseInt(taskNumberString);

                    String taskTitle = entry.getValue().getTitle();
                    String taskDescription = entry.getValue().getDescription();

                    int studyPlanId = studyPlanDAO.getStudyPlanByTitle(plan.getTitle()).getId();
                    StudyTask currentTask = new StudyTask(studyPlanId, taskNumber, taskTitle, taskDescription, "ACTIVE");
                    taskDAO.createTask(currentTask);
                }


                System.out.println("STUDY PLAN COMPLETED! \n");
                Platform.runLater(() -> {
                    try {
                        navigateTo("studyplanners");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                return "Success"; //

            }
        };

        ollamaTask.setOnSucceeded(event -> {

            generateStudyPlanButton.setDisable(false);
            promptEntryTextArea.setDisable(false);
            studyHoursEntryTextField.setDisable(false);
            progressIndicator.setVisible(false);
            // llmService.statusMessageProperty().set("Status: Response received.");
        });

        //fail
        ollamaTask.setOnFailed(event -> {

            Throwable exception = ollamaTask.getException();
            String errorMessage = "Error: " + (exception != null ? exception.getMessage() : "Unknown error");

            Platform.runLater(() -> {
                llmService.statusMessageProperty().set("Status: " + errorMessage);
                System.out.println("Error: " + errorMessage);
                generateStudyPlanButton.setDisable(false);
                promptEntryTextArea.setDisable(false);
                studyHoursEntryTextField.setDisable(false);
                progressIndicator.setVisible(false);
            });
            if (exception != null) {
                exception.printStackTrace();
            }
        });

        // Start task on new thread
        new Thread(ollamaTask).start();
    }

}