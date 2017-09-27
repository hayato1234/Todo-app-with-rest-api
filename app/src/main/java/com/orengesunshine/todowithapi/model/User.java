package com.orengesunshine.todowithapi.model;


public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private String api_key;
    private String status;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getApi_key() {
        return api_key;
    }

    public String getStatus() {
        return status;
    }
}
