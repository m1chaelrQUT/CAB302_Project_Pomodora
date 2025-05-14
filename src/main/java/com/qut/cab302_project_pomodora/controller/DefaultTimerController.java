package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.model.*;
import javafx.application.Platform;
import com.qut.cab302_project_pomodora.util.ThemeManager;
import com.qut.cab302_project_pomodora.config.Theme;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.sql.SQLException;

public class DefaultTimerController extends ControllerSkeleton {

    @FXML private StackPane defaultTimer;
    @FXML private Region contentPane;

    @FXML
    private Button startPauseButton, stopButton, resetButton, nextButton;
    @FXML
    private Label timerText;
    @FXML
    private Circle timerCircle;

    private User currentUser;

    // DAO interfaces
    private ITimerDAO timerDAO;
    private IUserDAO userDAO;

    public DefaultTimerController() {
        timerDAO = new SqliteTimerDAO();
        userDAO = new SqliteUserDAO();
    }


    @FXML private Region navbar;
    @FXML private NavbarController navbarController;
    private Timeline timeline;
    private int minutes;
    private int seconds;
    private boolean isRunning = false;

    private int pomodoroCount;
    private boolean isWorkSession;
    private int WORK_DURATION;
    private int SHORT_BREAK;
    private int LONG_BREAK;


    @Override
    protected StackPane getRootPane() {
        return defaultTimer;
    }

    @Override
    protected Region getContentPane() {
        return contentPane;
    }

    @Override
    public void initialize() throws SQLException, IOException {
        super.initialize();
        iniSession();

        // Initialize the timer values for the current user
        Timer userTimer = timerDAO.getUserTimer(currentUser);

        // Set the user timer values
        pomodoroCount = userTimer.getLongBreakAfter();
        WORK_DURATION = userTimer.getWorkDuration();
        SHORT_BREAK = userTimer.getShortBreakDuration();
        LONG_BREAK = userTimer.getLongBreakDuration();
        isWorkSession = true;

        // Set the initial timer values
        minutes = WORK_DURATION / 60;
        seconds = WORK_DURATION % 60;

        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        contentPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        Platform.runLater(() -> {
            navbarController.setNavButtonStyles(defaultTimer.getScene());
        });

        startPauseButton.setOnAction(event -> handleStartPause());
        stopButton.setOnAction(event -> handleStop());
        resetButton.setOnAction(event -> handleReset());
        nextButton.setOnAction(event -> handleNextPomodoro());


        updateTimerDisplay();

    }

    /**
     * Initializes the session by loading the current user from the session manager.
     * This method is called during the initialization of the controller.
     *
     * @throws SQLException if there is an error loading the session from the database
     * @throws IOException  if there is an error loading the session from the file
     */
    public void iniSession() throws SQLException, IOException {
        // Load the session to check if the user is already logged in
        SessionManager.loadSession();

        currentUser = SessionManager.getCurrentUser();
        System.out.println("Session loaded!");
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

            if (pomodoroCount % 4 == 0) {
                minutes = LONG_BREAK / 60;
            } else {
                minutes = SHORT_BREAK / 60;
            }
        } else {
            if (pomodoroCount % 4 == 0) {
                showCompletionMessage();
                pomodoroCount = 0;
                isWorkSession = true;
                minutes = WORK_DURATION / 60;
                seconds = WORK_DURATION % 60;
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
        pomodoroCount = 0;
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
            if (pomodoroCount % 4 == 0) {
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
}
