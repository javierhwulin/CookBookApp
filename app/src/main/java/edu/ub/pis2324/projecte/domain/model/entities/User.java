package edu.ub.pis2324.projecte.domain.model.entities;

import java.io.Serializable;

public class User implements Serializable {

    private String username;
    private String password;
    private String email;

    private boolean isPremium;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isPremium = false;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

}
