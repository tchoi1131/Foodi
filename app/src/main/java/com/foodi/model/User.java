package com.foodi.model;

/**
 * Created by Tom Wong on 1/30/2017.
 * This is the class to store the User Information
 */

public class User {
    private String userId;      //User ID
    private String email;       //email address
    private String userName;    //user name

    //constructors
    public User(){

    }

    /**
     * constructor to create User object base on user Id, email address and user name
     * @param userId: User Id
     * @param email: Email address
     * @param userName: User name
     */
    public User(String userId, String email, String userName){
        this.userId = userId;
        this.email = email;
        this.userName = userName;
    }

    //getters and setters
    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
