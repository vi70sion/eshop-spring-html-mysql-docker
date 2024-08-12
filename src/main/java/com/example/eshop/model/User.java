package com.example.eshop.model;

public class User {
    private long id;
    private String name;
    private String password;

    public User() {
    }

    public User(long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }


}
