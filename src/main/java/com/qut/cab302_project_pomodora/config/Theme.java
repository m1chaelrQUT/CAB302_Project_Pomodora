package com.qut.cab302_project_pomodora.config;

public enum Theme {
    LIGHT("light.css"),
    DARK("dark.css"),
    VOIDLIGHT("voidlight.css");

    private final String cssPath;

    Theme(String cssPath) {
        // We assume the path is relative to /resources/styles/
        this.cssPath = "/com/qut/cab302_project_pomodora/css/themes/" + cssPath;
    }

    /**
     * Gets the classpath resource path for the theme's CSS file.
     * Example: "/styles/themes/light.css"
     * @return The theme's CSS file path.
     */
    public String getCssPath() {
        return cssPath;
    }

    public Theme next() {
        Theme[] values = Theme.values();
        return values[(ordinal() + 1) % values.length];
    }
}
