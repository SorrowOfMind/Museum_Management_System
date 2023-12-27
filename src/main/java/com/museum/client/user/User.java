package com.museum.client.user;

import com.museum.models.Worker;

public class User {
    private String username;
    private int role;

    private Worker worker;

    public User(String username, int role, Worker worker) {
        this.username = username;
        this.role = role;
        this.worker = worker;
    }

    public Worker getUser() {
        return worker;
    }

    public String getUsername() {
        return username;
    }
    public int getRole() {
        return role;
    }
}
