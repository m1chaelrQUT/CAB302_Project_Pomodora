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

/**
 * DefaultTimerController is responsible for managing the default timer functionality
 * in the application. It handles user interactions, updates the timer display,
 * and manages the timer state (running, paused, stopped).
 */
public class DefaultTimerController extends ControllerSkeleton {

    // Constants for design dimensions
    @FXML private StackPane defaultTimer;
    @FXML private Region contentPane;

    // Buttons and labels for the timer
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

    /**
     * Constructor for DefaultTimerController.
     * Initializes the timer and user DAO interfaces.
     */
    public DefaultTimerController() {
        timerDAO = new SqliteTimerDAO();
        userDAO = new SqliteUserDAO();
    }


    /// FXML components
    @FXML private Region navbar;
    @FXML private NavbarController navbarController;

    // Timer variables
    private Timeline timeline;
    private int minutes;
    private int seconds;
    private boolean isRunning = false;

    // Timer settings
    private int longBreakAfter;
    private int pomodoroCount;
    private boolean isWorkSession;
    private int WORK_DURATION;
    private int SHORT_BREAK;
    private int LONG_BREAK;


    /**
     * Gets the design width of the timer.
     * @return The design width of the timer.
     */
    @Override
    protected StackPane getRootPane() {
        return defaultTimer;
    }

    /**
     * Gets the content pane of the timer.
     * @return The content pane of the timer.
     */
    @Override
    protected Region getContentPane() {
        return contentPane;
    }

    /**
     * Initializes the timer controller.
     * This method is called when the controller is loaded.
     * It sets up the initial state of the timer and binds event handlers to buttons.
     *
     * @throws SQLException if there is an error loading the session from the database
     * @throws IOException  if there is an error loading the session from the file
     */
    @Override
    public void initialize() throws SQLException, IOException {
        super.initialize();
        iniSession();

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

        // Set the initial styles for the navbar
        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        // Set the initial styles for the navbar
        contentPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Set the initial styles for the timer circle
        Platform.runLater(() -> {
            navbarController.setNavButtonStyles(defaultTimer.getScene());
        });

        // Set the initial timer display
        startPauseButton.setOnAction(event -> handleStartPause());
        stopButton.setOnAction(event -> handleStop());
        resetButton.setOnAction(event -> handleReset());
        nextButton.setOnAction(event -> handleNextPomodoro());

        // Set the initial timer display
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

    /**
     * Handles the start/pause button action.
     * This method is called when the start/pause button is clicked.
     * It starts or pauses the timer based on its current state.
     */
    private void handleStartPause() {
        if (isRunning) {
            pauseTimer();
        } else {
            startTimer();
        }
    }

    /**
     * Starts the timer.
     * This method is called when the start button is clicked.
     * It updates the button text and starts the timer animation.
     */
    private void startTimer() {
        isRunning = true;
        startPauseButton.setText("⏸");

        if (timeline == null) {
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
            timeline.setCycleCount(Timeline.INDEFINITE);
        }
        timeline.play();
    }

    /**
     * Pauses the timer.
     * This method is called when the pause button is clicked.
     * It updates the button text and pauses the timer animation.
     */
    private void pauseTimer() {
        isRunning = false;
        startPauseButton.setText("▶");

        if (timeline != null) {
            timeline.pause();
        }
    }

    /**
     * Handles the stop button action.
     * This method is called when the stop button is clicked.
     * It stops the timer and resets it to the initial state.
     */
    private void handleStop() {
        if (timeline != null) {
            timeline.stop();
        }
        isRunning = false;
        startPauseButton.setText("▶");
        resetTimer();
    }

    /**
     * Handles the reset button action.
     * This method is called when the reset button is clicked.
     * It resets the timer to the initial state.
     */
    private void handleReset() {
        resetTimer();
    }

    /**
     * Handles the next button action.
     * This method is called when the next button is clicked.
     * It updates the timer to the next Pomodoro session.
     */
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

    /**
     * Resets the timer to its initial state.
     * This method is called when the reset button is clicked.
     * It stops the timer and resets the timer values.
     */
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

    /**
     * Updates the timer display.
     * This method is called every second to update the timer display.
     * It decrements the timer values and updates the display accordingly.
     */
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

    /**
     * Handles the end of the timer.
     * This method is called when the timer reaches zero.
     * It updates the timer values and starts the next session.
     */
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

    /**
     * Updates the session style based on the current session type.
     * This method is called to change the color of the timer circle based on the session type.
     */
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

    /**
     * Shows a completion message when the Pomodoro cycle is complete.
     * This method is called to display a message when the user completes a full Pomodoro cycle.
     */
    private void showCompletionMessage() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Pomodoro Complete!");
        alert.setHeaderText(null);
        alert.setContentText("🎉 You've completed a full Pomodoro cycle! Take a well-deserved break.");
        alert.showAndWait();
    }

    /**
     * Updates the timer display with the current time.
     * This method is called to update the timer display with the current time.
     */
    private void updateTimerDisplay() {
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }

    /**
     * Sets the scene for the timer controller.
     * This method is called to set the scene for the timer controller.
     *
     * @param scene The scene to set for the timer controller.
     */
    public void setScene(Scene scene) {
        Theme currentTheme = ThemeManager.getInstance().getCurrentTheme();
        ThemeManager.getInstance().applyTheme(scene, Theme.LIGHT);
    }
}
