package com.qut.cab302_project_pomodora.model;

public interface ITimerDAO {
    /**
     * Initializes the user timers with default values.
     * @param user The user to initialize timers for.
     */
    public void initializeUserTimers(User user);

    /**
     * Updates the user timers with the given timer values.
     * @param user
     * @param timer
     */
    public void updateUserTimers(User user, Timer timer);

    public Timer getUserTimer(User user);

    void createUserTimer(User currentUser);
}
