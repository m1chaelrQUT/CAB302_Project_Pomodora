package com.qut.cab302_project_pomodora.controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

public class HomeController {

    // Resolution of design
    private static final double DESIGN_WIDTH = 1920.0;
    private static final double DESIGN_HEIGHT = 1080.0;

    @FXML
    private StackPane rootPane;

    @FXML
    private VBox contentPane;

    // fx ids of all content
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMeCheckBox;
    @FXML private Region topSpacer;
    @FXML private Region bottomSpacer;
    @FXML private Label brandLabel;
    @FXML private VBox signinPane;
    @FXML private Label ctaLabel;
    @FXML private Label ctaLogo;


    public void initialize() {
        //Set sizes
        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        contentPane.setMinSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        contentPane.setMaxSize(DESIGN_WIDTH, DESIGN_HEIGHT);


        // Use Platform.runLater to ensure scene is definitely available after layout
        Platform.runLater(() -> {
            Scene scene = rootPane.getScene();
            if (scene != null) {
                System.out.println("Scene obtained via Platform.runLater. Applying scaling listeners.");
                addScalingListeners(scene);
                updateScale(scene);
            } else {
                rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
                    if (newScene != null) {
                        System.out.println("Scene obtained via sceneProperty listener. Applying scaling listeners.");
                        addScalingListeners(newScene);
                        Platform.runLater(() -> updateScale(newScene));
                    } else if (oldScene != null) {
                        removeScalingListeners(oldScene);
                    }
                });
            }
        });

        System.out.println("HomeController Initialized.");
    }


    private ChangeListener<Number> widthListener;
    private ChangeListener<Number> heightListener;

    private void addScalingListeners(Scene scene) {
        // Create listeners
        if (widthListener == null) {
            widthListener = (obs, oldVal, newVal) -> Platform.runLater(() -> updateScale(scene)); // Ensure updates run on JavaFX thread
        }
        if (heightListener == null) {
            heightListener = (obs, oldVal, newVal) -> Platform.runLater(() -> updateScale(scene)); // Ensure updates run on JavaFX thread
        }

        // Remove old listeners
        removeScalingListeners(scene);

        // Add new listeners
        scene.widthProperty().addListener(widthListener);
        scene.heightProperty().addListener(heightListener);
        System.out.println("Added scaling listeners to Scene.");
    }

    private void removeScalingListeners(Scene scene) {
        if (scene == null) return;
        if (widthListener != null) {
            scene.widthProperty().removeListener(widthListener);
            // System.out.println("Removed width listener.");
        }
        if (heightListener != null) {
            scene.heightProperty().removeListener(heightListener);
            // System.out.println("Removed height listener.");
        }
    }

    private void updateScale(Scene scene) {
        if (scene == null || contentPane == null) {
            System.err.println("Cannot update scale: Scene or ContentPane is null.");
            return;
        }

        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();

        // Validate initial sizes.
        if (sceneWidth <= 1 || sceneHeight <= 1 || DESIGN_WIDTH <= 0 || DESIGN_HEIGHT <= 0) {
            System.out.println("Scene/Design dimensions invalid for scaling, skipping update.");
            // Reset scale if invalid dimensions
            contentPane.setScaleX(1.0);
            contentPane.setScaleY(1.0);
            return;
        }

        double scaleX = sceneWidth / DESIGN_WIDTH;
        double scaleY = sceneHeight / DESIGN_HEIGHT;

        //Bounds layoutBounds = contentPane.getLayoutBounds(); // Bounds before transforms (should be DESIGN_WIDTH x DESIGN_HEIGHT)

        contentPane.setScaleX(scaleX);
        contentPane.setScaleY(scaleY);
        /*
        // Use Platform.runLater to get bounds *after* the scale is likely applied and layout pass potentially run
        Platform.runLater(() -> {
            Bounds boundsInParent = contentPane.getBoundsInParent(); // Bounds after transforms, relative to StackPane
            double renderedWidth = boundsInParent.getWidth();
            double renderedHeight = boundsInParent.getHeight();

            System.out.printf("Scene: %.1fx%.1f | Scale Calc: X=%.4f Y=%.4f | LayoutBounds: %.1fx%.1f | Rendered BoundsInParent: %.1fx%.1f%n",
                    sceneWidth, sceneHeight,
                    scaleX, scaleY,
                    layoutBounds.getWidth(), layoutBounds.getHeight(), // Should stay 1920x1080
                    renderedWidth, renderedHeight // Should approximate sceneWidth x sceneHeight
            );

            // Check if rendered size significantly deviates from scene size
            if (Math.abs(renderedWidth - sceneWidth) > 2.0 || Math.abs(renderedHeight - sceneHeight) > 2.0) {
                System.err.printf("WARN: Rendered size (%.1f x %.1f) significantly differs from Scene size (%.1f x %.1f)%n",
                        renderedWidth, renderedHeight, sceneWidth, sceneHeight);
            }
        });
        */
    }

    @FXML
    private void handleSignIn() {
        System.out.println("HomeController handleSignIn");
        System.out.println("Username Entered: " + usernameField.getText());
        System.out.println("Password Entered: " + passwordField.getText());
        System.out.println("Remember Me? " + rememberMeCheckBox.isSelected());
    }
}