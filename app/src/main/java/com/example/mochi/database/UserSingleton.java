package com.example.mochi.database;

import com.example.mochi.model.User;

public class UserSingleton {

    private static final UserSingleton instance = new UserSingleton();

    private User user;

    private UserSingleton() {

    }

    public static UserSingleton getInstance() {
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
