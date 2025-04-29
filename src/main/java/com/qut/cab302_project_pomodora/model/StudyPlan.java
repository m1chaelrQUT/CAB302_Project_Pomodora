package com.qut.cab302_project_pomodora.model;

import java.util.List;



public class StudyPlan
{
    private int id;
    private int userId;
    private String title;
    private String description;
    private String status;
    private List<Task> tasks;
    private int participantCount;

    public  StudyPlan()
    {}

    public StudyPlan(int userId,String title,String description,String status)
    {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id; //hard code this shit
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Task> tasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    @Override
    public String toString() {
        return "StudyPlan{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", participantCount=" + participantCount +
                '}';
    }
}