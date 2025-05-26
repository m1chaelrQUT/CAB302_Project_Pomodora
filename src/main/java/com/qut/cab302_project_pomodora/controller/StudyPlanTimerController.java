package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.model.*;
import javafx.application.Platform;
import com.qut.cab302_project_pomodora.util.ThemeManager;
import com.qut.cab302_project_pomodora.config.Theme;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.shape.Circle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;


import java.beans.beancontext.BeanContextServiceRevokedEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class StudyPlanTimerController extends ControllerSkeleton {
    @FXML private StackPane studyPlanTimer;
    @FXML private Region contentPane;

    @FXML
    private Button startPauseButton, stopButton, resetButton, nextButton;
    @FXML
    private Label timerText;
    @FXML
    private Circle timerCircle;


    @FXML private Region navbar;
    @FXML private NavbarController navbarController;
    @FXML private Button plusButton;
    @FXML private Label studyPlanTitleLabel;
    @FXML private StackPane selectPlanModal;
    @FXML private VBox taskSideBar;
    @FXML private VBox taskListContainer;

    private SqliteStudyPlanDAO studyPlanDAO = new SqliteStudyPlanDAO();
    private SqliteTaskDAO taskDAO = new SqliteTaskDAO();
    private Timeline pomodoroTimeline;

    private StudyPlan activeStudyPlan;
    private List<Task> activeTasks;
    private int currentTaskIndex = 0;

    private Timeline timeline;
    private int minutes = 25;  // Default work duration (Pomodoro technique)
    private int seconds = 0;
    private boolean isRunning = false;

    private int pomodoroCount = 0;
    private int WORK_DURATION = 25;
    private int SHORT_BREAK = 5;
    private int LONG_BREAK = 15; // You can make this customizable
    private boolean isWorkSession = true;

    @FXML
    private void handleCloseModal() {
        selectPlanModal.setVisible(false);
        selectPlanModal.setManaged(false);
        selectPlanModal.setMouseTransparent(true);
        System.out.println("Closing Modal");
        System.out.println("selectPlanModal =" + selectPlanModal);
    }

    @FXML
    public void handlePlusButtonClicked() {
        System.out.println("Plus button actually clicked");
        selectPlanModal.setVisible(true);
        selectPlanModal.setManaged(true);
        selectPlanModal.setMouseTransparent(false);
    }


    @Override
    protected StackPane getRootPane() {
        return studyPlanTimer;
    }

    @Override
    protected Region getContentPane() {
        return contentPane;
    }

    @Override
    public void initialize() throws SQLException, IOException {
        super.initialize();

        try {
            checkActiveStudyPlan();
            //loadFirstStudyPlanFromDatabase();
            System.out.println("Checked active study plan.");
        } catch (Exception e) {
            e.printStackTrace(); // catch and log initialization exceptions
        }

        System.out.println("Initializing StudyPlanTimerController...");

        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        contentPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        Platform.runLater(() -> {
            navbarController.setNavButtonStyles(studyPlanTimer.getScene());
        });

        startPauseButton.setOnAction(event -> handleStartPause());
        stopButton.setOnAction(event -> handleStop());
        resetButton.setOnAction(event -> handleReset());
        nextButton.setOnAction(event -> handleNextPomodoro());


        updateTimerDisplay();

    }
    private void handleStartPause() {
        if (isRunning) {
            pauseTimer();
        } else {
            startTimer();
        }

        if (pomodoroTimeline == null || pomodoroTimeline.getStatus() == Timeline.Status.STOPPED) {
            pomodoroTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
            pomodoroTimeline.setCycleCount(Timeline.INDEFINITE);
            pomodoroTimeline.play();
        } else {
            pomodoroTimeline.stop();
        }
    }

    private void startTimer() {
        isRunning = true;
        startPauseButton.setText("⏸");

        if (timeline == null) {
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
            timeline.setCycleCount(Timeline.INDEFINITE);
        }
        timeline.play();
    }

    private void pauseTimer() {
        isRunning = false;
        startPauseButton.setText("▶");

        if (timeline != null) {
            timeline.pause();
        }
    }

    private void handleStop() {
        if (timeline != null) {
            timeline.stop();
        }
        isRunning = false;
        startPauseButton.setText("▶");
        resetTimer();
    }

    private void handleReset() {
        resetTimer();
    }

    private void handleNextPomodoro() {
        if (isWorkSession) {
            pomodoroCount++;

            if (pomodoroCount % 4 == 0) {
                minutes = LONG_BREAK;
            } else {
                minutes = SHORT_BREAK;
            }
        } else {
            if (pomodoroCount % 4 == 0) {
                showCompletionMessage();
                pomodoroCount = 0;
                isWorkSession = true;
                minutes = WORK_DURATION;
                seconds = 0;
                isRunning = false;

                if (timeline != null) timeline.stop();
                startPauseButton.setText("▶");
                updateSessionStyle();
                updateTimerDisplay();
                return;
            }
            minutes = WORK_DURATION;
        }

        seconds = 0;
        isWorkSession = !isWorkSession;
        updateSessionStyle();
        updateTimerDisplay();
        updateTaskVisuals();
        currentTaskIndex++;
    }

    private void resetTimer() {
        if (timeline != null) {
            timeline.stop();
        }
        isRunning = false;
        pomodoroCount = 0;
        isWorkSession = true;
        minutes = WORK_DURATION;
        seconds = 0;

        startPauseButton.setText("▶");
        updateSessionStyle();
        updateTimerDisplay();
    }

    private void updateTimer() {
        if (seconds == 0) {
            if (minutes == 0) {
                timeline.stop();
                isRunning = false;
                startPauseButton.setText("▶");

                handleTimerEnd();
                return;
            } else {
                minutes--;
                seconds = 59;
            }
        } else {
            seconds--;
        }
        updateTimerDisplay();
    }

    private void handleTimerEnd() {
        if (isWorkSession) {
            pomodoroCount++;
            if (pomodoroCount % 4 == 0) {
                minutes = LONG_BREAK;
            } else {
                minutes = SHORT_BREAK;
            }
        } else {
            minutes = WORK_DURATION;
        }
        seconds = 0;
        isWorkSession = !isWorkSession;

        updateSessionStyle();
        updateTimerDisplay();
        startTimer();
    }

    private void updateSessionStyle() {
        if (timerCircle != null) {
            if (isWorkSession) {
                timerCircle.setFill(Color.web("#ff9a8b")); // Light red/orange for work
            } else if (pomodoroCount % 4 == 0) {
                timerCircle.setFill(Color.web("#8bbaff")); // Blue for long break
            } else {
                timerCircle.setFill(Color.web("#91d18b")); // Green for short break
            }
        }
    }
    private void showCompletionMessage() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Pomodoro Complete!");
        alert.setHeaderText(null);
        alert.setContentText("🎉 You've completed a full Pomodoro cycle! Take a well-deserved break.");
        alert.showAndWait();
    }

    private void updateTimerDisplay() {
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }

    public void setScene(Scene scene) {
        Theme currentTheme = ThemeManager.getInstance().getCurrentTheme();
        ThemeManager.getInstance().applyTheme(scene, Theme.LIGHT);
    }

    private void checkActiveStudyPlan() {
        User currentUser = SessionManager.getCurrentUser();
        List<StudyPlan> plans = studyPlanDAO.getStudyPlansByStatus(currentUser.getId(), "ACTIVE");

        if (plans == null || plans.isEmpty()) {
            showNoActivePlanUI();
        } else {
            activeStudyPlan = plans.get(0); // assume only one ACTIVE plan
            activeTasks = taskDAO.getTasksByStudyPlan(activeStudyPlan.getId());
            showTimerUI();
            renderTasks();
        }
    }


    public void loadUserTimer(com.qut.cab302_project_pomodora.model.Timer timer) {
        this.WORK_DURATION = timer.getWorkDuration();
        this.SHORT_BREAK = timer.getShortBreakDuration();
        this.LONG_BREAK = timer.getLongBreakDuration();

        this.minutes = WORK_DURATION;
        this.seconds = 0;
        this.pomodoroCount = 0;
        this.isWorkSession = true;

        updateSessionStyle();
        updateTimerDisplay();
    }

    private void showNoActivePlanUI() {
        plusButton.setVisible(true);plusButton.setManaged(true);
        plusButton.setMouseTransparent(false);
        startPauseButton.setVisible(false);
        stopButton.setVisible(false);
        resetButton.setVisible(false);
        nextButton.setVisible(false);
        timerText.setVisible(false);
        timerCircle.setVisible(false);

        studyPlanTitleLabel.setVisible(false);
        taskSideBar.setVisible(false);
    }

    private void showTimerUI() {
        plusButton.setVisible(true);

        startPauseButton.setVisible(true);
        stopButton.setVisible(true);
        resetButton.setVisible(true);
        nextButton.setVisible(true);
        timerText.setVisible(true);
        timerCircle.setVisible(true);
        plusButton.setVisible(false);

        studyPlanTitleLabel.setText(activeStudyPlan.getTitle());
        studyPlanTitleLabel.setVisible(true);
        taskSideBar.setVisible(true);
    }

    private void renderTasks() {
        // Look up taskListContainer dynamically from the taskSideBar
        VBox taskListContainer = (VBox) taskSideBar.lookup("#taskListContainer");

        if (taskListContainer == null) {
            System.out.println("❌ taskListContainer not found inside taskSideBar!");
            return;
        }

        if (activeTasks == null) {
            System.out.println("❌ activeTasks is null.");
            return;
        }

        taskListContainer.getChildren().clear();

        for (int i = 0; i < activeTasks.size(); i++) {
            Task task = activeTasks.get(i);
            HBox taskBox = new HBox();
            taskBox.setSpacing(10);
            taskBox.setAlignment(Pos.CENTER_LEFT);
            taskBox.setPadding(new Insets(10));
            taskBox.setStyle("-fx-border-color: #ccc; -fx-border-width: 0 0 1 0;");

            VBox textBox = new VBox();
            textBox.setAlignment(Pos.TOP_LEFT);

            Label titleLabel = new Label(task.getTitle());
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

            Label descLabel = new Label(task.getDescription());
            descLabel.setWrapText(true);
            descLabel.setStyle("-fx-font-size: 14px;");

            textBox.getChildren().addAll(titleLabel, descLabel);
            HBox.setHgrow(textBox, Priority.ALWAYS);

            Circle statusCircle = new Circle(6);
            statusCircle.setFill(i < currentTaskIndex ? Color.GREEN :
                    (i == currentTaskIndex ? Color.GOLD : Color.RED));

            taskBox.getChildren().addAll(statusCircle, textBox);
            taskListContainer.getChildren().add(taskBox);
        }
    }




    private void updateTaskVisuals() {
        renderTasks();
    }

    private void loadFirstStudyPlanFromDatabase() {
        try {
            User user = SessionManager.getCurrentUser(); // simulate login
            List<StudyPlan> plans = studyPlanDAO.getStudyPlansByStatus(user.getId(), "ACTIVE");

            if (plans == null || plans.isEmpty()) {
                System.out.println("⚠ No active study plans found.");
                return;
            }

            activeStudyPlan = plans.get(0);
            activeTasks = taskDAO.getTasksByStudyPlan(activeStudyPlan.getId());

            showTimerUI();
            renderTasks();

            System.out.println("✅ Loaded Study Plan: " + activeStudyPlan.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}