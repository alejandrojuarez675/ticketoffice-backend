package com.ticketoffice.backend.domain.enums;

public enum UserRole {
    ADMIN("ADMIN"),
    SELLER("SELLER"),
    USER("USER");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
