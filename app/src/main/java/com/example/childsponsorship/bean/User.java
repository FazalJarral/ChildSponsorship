package com.example.childsponsorship.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    String name;
    String email;
    boolean isSponser;
    String department;
    String userId;
    String token;

    public User() {
    }

    public User(String name, String email, boolean isSponser, String department) {
        this.name = name;
        this.email = email;
        this.isSponser = isSponser;
        this.department = department;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSponser() {
        return isSponser;
    }

    public void setSponser(boolean sponser) {
        isSponser = sponser;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    @NonNull
    @Override
    public String toString() {
        return name ;
    }
}
