package com.qut.cab302_project_pomodora.model;

public class Task {
    private int id;
    private int studyPlanId;
    private int taskNumber;
    public String title;
    private String description;
    private String status;

    public Task(int studyPlanId, int taskNumber, String title, String description, String status) {
        this.studyPlanId = studyPlanId;
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

    public void setStudyPlanId(int studyPlanId) {
        this.studyPlanId = studyPlanId;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
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


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", studyPlanId=" + studyPlanId +
                ", title='" + title + '\'' +
                ", status='" + status +
                '}';
    }
}




