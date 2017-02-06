package com.foodi.model;

import java.util.ArrayList;

/**
 * Created by Tom Wong on 1/30/2017.
 */

public class User {
    private String userId;
    private String email;
    private String userName;
    private ArrayList<Integer> roles;

    //constructors
    public User(String userId, String email, String userName, ArrayList<Integer> roles){
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.roles = roles;
    }

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

    public ArrayList<Integer> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Integer> roles) {
        this.roles = roles;
    }
}
