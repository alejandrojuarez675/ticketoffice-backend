package com.ticketoffice.backend.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ticketoffice.backend.domain.enums.UserRole;

import java.util.List;
import java.util.Optional;

public class User {

    private String id;
    private String username;
    private String email;
    private String password;
    private List<UserRole> role;
    private Organizer organizer;
    private boolean confirmed;

    public User(String id, String username, String email, String password, List<UserRole> role, Organizer organizer) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.organizer = organizer;
        this.confirmed = false;
    }

    public User(String id, String username, String email, String password, List<UserRole> role, Organizer organizer, Boolean confirmed) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.organizer = organizer;
        this.confirmed = confirmed;
    }

    public String getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserRole> getRole() {
        return role;
    }

    public void setRole(List<UserRole> role) {
        this.role = role;
    }

    public void setId(String string) {
        id = string;
    }

    public Optional<Organizer> getOrganizer() {
        return Optional.ofNullable(organizer);
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    @JsonIgnore
    public boolean isAdmin() {
        return role.contains(UserRole.ADMIN);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
