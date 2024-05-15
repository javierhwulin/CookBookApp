package edu.ub.pis2324.projecte.domain.model.entities;

import java.io.Serializable;

import edu.ub.pis2324.projecte.domain.model.values.ClientId;

public class User implements Serializable {
    private ClientId id;
    private String username;
    private String password;
    private String email;

    private boolean isPremium;

    public User(ClientId id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isPremium = false;
    }

    public User() {
    }

    public ClientId getId() {
        return id;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
