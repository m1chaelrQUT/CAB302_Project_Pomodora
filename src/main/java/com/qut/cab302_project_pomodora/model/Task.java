package com.qut.cab302_project_pomodora.model;

public class Task {
    private int id;
    private int studyPlanId;
    private int userId;
    public String title;
    private String description;
    private String status;
    private boolean isSticky;


    public Task() {}

    public Task(int studyPlanId, int userId, String title, String description, String status) {
        this.studyPlanId = studyPlanId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudyPlanId() {
        return studyPlanId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setStudyPlanId(int studyPlanId) {
        this.studyPlanId = studyPlanId;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSticky() {
        return isSticky;
    }

    public void setSticky(boolean sticky) {
        isSticky = sticky;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", studyPlanId=" + studyPlanId +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", isSticky=" + isSticky +
                '}';
    }
}




