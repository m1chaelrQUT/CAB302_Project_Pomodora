package com.qut.cab302_project_pomodora.model;

public class Timer {
    private int id;
    private int userId;
    private int workDuration;
    private int shortBreakDuration;
    private int longBreakDuration;
    private int longBreakAfter;

    public Timer(int workDuration, int shortBreakDuration, int longBreakDuration, int longBreakAfter) {
        this.workDuration = workDuration;
        this.shortBreakDuration = shortBreakDuration;
        this.longBreakDuration = longBreakDuration;
        this.longBreakAfter = longBreakAfter;
    }


    /**
     * Getters and Setters of Variables
     * @return
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(int workDuration) {
        this.workDuration = workDuration;
    }

    public int getShortBreakDuration() {
        return shortBreakDuration;
    }

    public void setShortBreakDuration(int shortBreakDuration) {
        this.shortBreakDuration = shortBreakDuration;
    }

    public int getLongBreakDuration() {
        return longBreakDuration;
    }

    public void setLongBreakDuration(int longBreakDuration) {
        this.longBreakDuration = longBreakDuration;
    }

    public int getLongBreakAfter() {
        return longBreakAfter;
    }

    public void setLongBreakAfter(int longBreakAfter) {
        this.longBreakAfter = longBreakAfter;
    }
}

