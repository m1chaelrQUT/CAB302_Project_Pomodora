package com.qut.cab302_project_pomodora.model;

public interface ITimerDAO {
    /**
     * Initializes the user timers with default values.
     * @param user The user to initialize timers for.
     */
    public void initializeUserTimers(User user);

    /**
     * Updates the user timers with the given values.
     * @param user
     * @param workDuration
     * @param shortBreakDuration
     * @param longBreakDuration
     * @param longBreakAfter
     */
    public void updateUserTimers(User user, int workDuration, int shortBreakDuration, int longBreakDuration, int longBreakAfter);
}
