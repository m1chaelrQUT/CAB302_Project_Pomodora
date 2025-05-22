package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.model.*;
import javafx.application.Platform;
import com.qut.cab302_project_pomodora.util.ThemeManager;
import com.qut.cab302_project_pomodora.config.Theme;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.shape.Circle;

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
    @FXML private AnchorPane taskSideBar;

    private ITimerDAO timerDAO;
    private IUserDAO userDAO;
    private IStudyPlanDAO studyPlanDAO;
    private ITaskDAO taskDAO;

    private StudyPlan activeStudyPlan;
    private List<StudyTask> activeTasks;

    public StudyPlanTimerController() {
        this.timerDAO = new SqliteTimerDAO();
        this.userDAO = new SqliteUserDAO();
        this.studyPlanDAO = new SqliteStudyPlanDAO();
        this.taskDAO = new SqliteTaskDAO();
    }

    private Timeline timeline;
    private int minutes;
    private int seconds;
    private boolean isRunning = false;

    private int longBreakAfter;
    private int pomodoroCount;
    private int WORK_DURATION;
    private int SHORT_BREAK;
    private int LONG_BREAK; // You can make this customizable
    private boolean isWorkSession;

    private User currentUser;


    @FXML
    private void handleCloseModal() {
        selectPlanModal.setVisible(false);
        selectPlanModal.setManaged(false);
        selectPlanModal.setMouseTransparent(true);
        try{
            navigateTo("studyplanners");
        } catch (Exception e){
            e.printStackTrace();
        }

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
        currentUser = SessionManager.getCurrentUser();
        if(currentUser == null) {
            throw new IllegalStateException("Current user is null. Cannot load study plans.");
        }

        // Initialize the timer values for the current user
        Timer userTimer = timerDAO.getUserTimer(currentUser);

        // Set the user timer values
        pomodoroCount = 0;
        longBreakAfter = userTimer.getLongBreakAfter();
        WORK_DURATION = userTimer.getWorkDuration();
        SHORT_BREAK = userTimer.getShortBreakDuration();
        LONG_BREAK = userTimer.getLongBreakDuration();
        isWorkSession = true;

        // Set the initial timer values
        minutes = WORK_DURATION / 60;
        seconds = WORK_DURATION % 60;

        checkActiveStudyPlan();

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

            if (pomodoroCount % longBreakAfter == 0) {
                minutes = LONG_BREAK / 60;
            } else {
                minutes = SHORT_BREAK / 60;
            }
        } else {
            if (pomodoroCount % longBreakAfter == 0) {
                showCompletionMessage();
                pomodoroCount = 1;
                isWorkSession = true;
                minutes = WORK_DURATION / 60;
                seconds = 0 % 60;
                isRunning = false;

                if (timeline != null) timeline.stop();
                startPauseButton.setText("▶");
                updateSessionStyle();
                updateTimerDisplay();
                return;
            }
            minutes = WORK_DURATION / 60;
        }

        seconds = WORK_DURATION % 60;
        isWorkSession = !isWorkSession;
        updateSessionStyle();
        updateTimerDisplay();
    }

    private void resetTimer() {
        if (timeline != null) {
            timeline.stop();
        }
        isRunning = false;
        pomodoroCount = 1;
        isWorkSession = true;
        minutes = WORK_DURATION / 60;
        seconds = WORK_DURATION % 60;

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
            if (pomodoroCount % longBreakAfter == 0) {
                minutes = LONG_BREAK / 60;
            } else {
                minutes = SHORT_BREAK / 60;
            }
        } else {
            minutes = WORK_DURATION / 60;
        }
        seconds = WORK_DURATION % 60;
        isWorkSession = !isWorkSession;

        updateSessionStyle(); // 🔥 Add this
        updateTimerDisplay();
        startTimer();
    }

    private void updateSessionStyle() {
        if (timerCircle != null) {
            if (isWorkSession) {
                timerCircle.setFill(Color.web("#ff9a8b")); // Light red/orange for work
            } else if (pomodoroCount % longBreakAfter == 0) {
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
        List<StudyPlan> plans = studyPlanDAO.getStudyPlansByStatus(currentUser.getId(), "RESUME");

        if (plans == null || plans.isEmpty()) {
            showNoActivePlanUI();
        } else {
            activeStudyPlan = plans.get(0);
            activeTasks = taskDAO.getTasksByStudyPlan(activeStudyPlan.getId());
            showTimerUI();
        }
    }

    private void changeActiveStudyPlan() {
        User currentUser = SessionManager.getCurrentUser();

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
        plusButton.setVisible(false);
        plusButton.setManaged(false);
        plusButton.setMouseTransparent(true);

        startPauseButton.setVisible(true);
        stopButton.setVisible(true);
        resetButton.setVisible(true);
        nextButton.setVisible(true);
        timerText.setVisible(true);
        timerCircle.setVisible(true);

        studyPlanTitleLabel.setText(activeStudyPlan.getTitle());
        studyPlanTitleLabel.setVisible(true);
        taskSideBar.setVisible(true);
    }
}