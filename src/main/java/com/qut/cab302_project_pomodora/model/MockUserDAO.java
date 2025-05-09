package com.qut.cab302_project_pomodora.model;

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
//        if(users.isEmpty()) {
//            addUser(new User("MicRob", "Michael", 1, 0, "MicRob123@example.com"));
//            addUser(new User("EthHun", "Ethan", 1, 0, "EthHun123@example.com"));
//            addUser(new User("BriKan", "Brian", 1, 0, "BriKan123@example.com"));
//            addUser(new User("SriKri", "Sriman", 1, 0, "SriKri123@example.com"));
//            addUser(new User("ShaTow", "Shane", 1, 0, "ShaTow123@example.com"));
//        }
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
    public User getUserByName(String userName) {
        for (User user : users) {
            if(user.getUserName().equals(userName)){
                return user;
            }
        }
        System.out.println("Username Entered: " + userName + " does not exist");
        return null;
    }
    @Override
    public User getUserByEmail(String userEmail) {
        for (User user : users) {
            if(user.getEmail().equals(userEmail)){
                return user;
            }
        }
        System.out.println("Email Entered: " + userEmail + " does not exist");
        return null;
    }
    @Override
    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                return;
            }
        }
        System.out.println("User with ID: " + user.getId() + " does not exist");
    }
}
