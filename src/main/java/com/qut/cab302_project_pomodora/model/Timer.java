package com.qut.cab302_project_pomodora.model;

/**
 * Timer class represents a timer with work duration, short break duration,
 * long break duration, and the number of work sessions before a long break.
 */
public class Timer {
    private int id;
    private int userId;
    private int workDuration;
    private int shortBreakDuration;
    private int longBreakDuration;
    private int longBreakAfter;

    /**
     * Constructor for Timer.
     * Initializes the timer with default values.
     */
    public Timer(int workDuration, int shortBreakDuration, int longBreakDuration, int longBreakAfter) {
        this.workDuration = workDuration;
        this.shortBreakDuration = shortBreakDuration;
        this.longBreakDuration = longBreakDuration;
        this.longBreakAfter = longBreakAfter;
    }

    /**
     * Getter for the userId.
     * @return userId
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the userId.
     * @param id userId
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for the workDuration.
     * @return workDuration
     */
    public int getWorkDuration() {
        return workDuration;
    }

    /**
     * Setter for the workDuration.
     * @param workDuration work duration
     */
    public void setWorkDuration(int workDuration) {
        this.workDuration = workDuration;
    }

    /**
     * Getter for the shortBreakDuration.
     * @return shortBreakDuration
     */
    public int getShortBreakDuration() {
        return shortBreakDuration;
    }

    /**
     * Setter for the shortBreakDuration.
     * @param shortBreakDuration short break duration
     */
    public void setShortBreakDuration(int shortBreakDuration) {
        this.shortBreakDuration = shortBreakDuration;
    }

    /**
     * Getter for the longBreakDuration.
     * @return longBreakDuration
     */
    public int getLongBreakDuration() {
        return longBreakDuration;
    }

    /**
     * Setter for the longBreakDuration.
     * @param longBreakDuration long break duration
     */
    public void setLongBreakDuration(int longBreakDuration) {
        this.longBreakDuration = longBreakDuration;
    }

    /**
     * Getter for the longBreakAfter.
     * @return longBreakAfter
     */
    public int getLongBreakAfter() {
        return longBreakAfter;
    }

    /**
     * Setter for the longBreakAfter.
     * @param longBreakAfter long break after
     */
    public void setLongBreakAfter(int longBreakAfter) {
        this.longBreakAfter = longBreakAfter;
    }
}

