package com.example.fefs;

public class User {
    protected int userId;
    protected String name;
    protected String email;
    protected String password;
    protected String role;

    public User(int userId, String name, String email, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void login(String email, String password){}
    public void logout(){}
}
