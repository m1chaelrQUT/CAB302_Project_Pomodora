package com.qut.cab302_project_pomodora.model;

/**
 * Interface for the User Data Access Object that handles
 * the CRUD operations for the User class with the database
 */
public interface IUserDAO {
    /**
     * Adds a new user to the database.
     * @param user The user to add.
     */
    public void addUser(User user);

    /**
     * Retrieves a user from the database.
     * @param userName The userName of the user to retrieve.
     * @return The user with the given userName
     */
    public User getUserByName(String userName);

    /**
     * Retrieves a user from the database.
     * @param email The email of the user to retrieve
     * @return The user with the given email
     */
    public User getUserByEmail(String email);

}
