package com.ticketoffice.backend.application.services;

import java.util.Optional;

public class PasswordEncoder {
    public String encode(String password) {
        return password;
    }

    public boolean matches(String password, String passwordEncoded) {
        return Optional.ofNullable(password)
                .map(this::encode)
                .map(x -> passwordEncoded.equals(password))
                .orElse(false);
    }
}
