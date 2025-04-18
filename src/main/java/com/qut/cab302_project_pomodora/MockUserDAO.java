package com.qut.cab302_project_pomodora;

import java.util.ArrayList;

public class MockUserDAO implements IUserDAO {
    /**
     * A mock database of static Users to be used in testing
     * and development of the sign-in page
     */
    public static final ArrayList<User> users = new ArrayList<>();
    private static int autoIncrementId = 0;

    public MockUserDAO() {
        // Initial Users - only if there are none in the Mock DB
        if(users.isEmpty()) {
            addUser(new User("MicRob", "Michael", 1, 0));
            addUser(new User("EthHun", "Ethan", 1, 0));
            addUser(new User("BriKan", "Brian", 1, 0));
            addUser(new User("SriKri", "Sriman", 1, 0));
            addUser(new User("ShaTow", "Shane", 1, 0));
        }
    }

    /**
     * Add a new user to the database
     * @param user The user to add.
     */
    @Override
    public void addUser(User user){
        user.setId(autoIncrementId);
        autoIncrementId++;
        users.add(user);
    }

    /**
     * Check if the userName exists
     * @param userName The userName of the user to retrieve.
     * @return the user if they exist, else error
     */
    @Override
    public User getUser(String userName) {
        for (User user : users) {
            if(user.getUserName().equals(userName)){
                return user;
            }
        }
        System.out.println("Username Entered: " + userName + " does not exist");
        return null;
    }
}
