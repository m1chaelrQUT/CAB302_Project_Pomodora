package com.qut.cab302_project_pomodora.model;

import java.util.ArrayList;
import java.util.List;



public class StudyPlan
{
    private int id;
    private int userId;
    private String title;
    private String description;
    private String status;
    private List<StudyTask> studyTasks;

    public  StudyPlan()
    {
        this.studyTasks = new ArrayList<>();
    }

    public StudyPlan(int id, int userId,String title,String description,String status)
    {
        this();
        this.id = id;
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

    public List<StudyTask> tasks() {
        return studyTasks;
    }

    public void setTasks(List<StudyTask> studyTasks) {
        this.studyTasks = studyTasks;
    }


    @Override
    public String toString() {
        return "StudyPlan{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public boolean planIsActive() {
        return status.equals("ACTIVE");
    }
}