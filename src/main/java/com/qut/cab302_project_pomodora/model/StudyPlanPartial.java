package com.qut.cab302_project_pomodora.model;

public class StudyPlanPartial {
    private String title;
    private String description;

    public StudyPlanPartial() {}

    public StudyPlanPartial(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
