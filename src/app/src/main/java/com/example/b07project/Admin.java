package com.example.b07project;

public class Admin {
    private String username;
    private String password;


    public Admin(){}
    public Admin(String username, String password) {
        this.username = username;
        this.password = password;

    }

    //getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    //setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
