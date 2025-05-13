package com.qut.cab302_project_pomodora.model;

public interface ITimerDAO {
    /**
     * Updates the user timers with the given timer values.
     * @param user
     * @param timer
     */
    void updateUserTimers(User user, Timer timer);

    /**
     * Retrieves the user timer from the database.
     * @param user
     * @return The timer for the given user
     */
    Timer getUserTimer(User user);

    /**
     * Creates a new user timer in the database.
     * @param currentUser
     */
    void createUserTimer(User currentUser);
}
