package com.qut.cab302_project_pomodora.util;

import javafx.scene.Scene;
import com.qut.cab302_project_pomodora.config.Theme;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThemeManager {

    private static final Logger LOGGER = Logger.getLogger(ThemeManager.class.getName());
    private static final String BASE_CSS_PATH = "/com/qut/cab302_project_pomodora/css/common/base.css";
    private static ThemeManager instance;

    private Theme currentTheme = Theme.LIGHT; // Default theme

    private ThemeManager() { }

    /**
     * Gets the instance of the ThemeManager.
     * @return The ThemeManager instance.
     */
    public static synchronized ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    /**
     * Applies the specified theme to the given scene.
     * It first applies the base stylesheet, then the theme-specific stylesheet.
     * @param scene The Scene to apply the theme to.
     * @param theme The Theme to apply.
     */
    public void applyTheme(Scene scene, Theme theme) {
        if (scene == null || theme == null) {
            LOGGER.log(Level.WARNING, "Scene or Theme is null, cannot apply theme.");
            return;
        }

        scene.getStylesheets().clear();

        // 1. Apply Base CSS
        String baseUrl = Objects.requireNonNull(getClass().getResource(BASE_CSS_PATH), "Base CSS not found: " + BASE_CSS_PATH).toExternalForm();
        scene.getStylesheets().add(baseUrl);
        //LOGGER.log(Level.INFO, "Applied base stylesheet: " + BASE_CSS_PATH);


        // 2. Apply Theme CSS
        String themeUrl = Objects.requireNonNull(getClass().getResource(theme.getCssPath()), "Theme CSS not found: " + theme.getCssPath()).toExternalForm();
        scene.getStylesheets().add(themeUrl);
        //LOGGER.log(Level.INFO, "Applied theme stylesheet: " + theme.getCssPath());


        // Update the current theme
        this.currentTheme = theme;
        //LOGGER.log(Level.INFO, "Current theme set to: " + theme.name());

        // Optional: Store scene reference if you need to update all open scenes later
        // (Requires managing a list of active scenes)
    }

    /**
     * Applies the currently stored theme to the given scene.
     * @param scene The Scene to apply the current theme to.
     */
    public void applyCurrentTheme(Scene scene) {
        applyTheme(scene, this.currentTheme);
    }

    /**
     * Sets the current theme preference. Does not automatically update existing scenes.
     * Call applyTheme or applyCurrentTheme on specific scenes to see the change.
     * @param theme The theme to set as current.
     */
    public void setCurrentTheme(Theme theme) {
        if (theme != null) {
            this.currentTheme = theme;
            //LOGGER.log(Level.INFO, "Default theme preference changed to: " + theme.name());
        }
    }

    /**
     * Gets the currently selected theme preference.
     * @return The current Theme.
     */
    public Theme getCurrentTheme() {
        return currentTheme;
    }
}
