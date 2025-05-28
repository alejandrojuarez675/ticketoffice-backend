package com.ticketoffice.backend.infra.adapters.in.exception;

public class UnauthorizedUserException extends Exception {
    public UnauthorizedUserException(String message) {
        super(message);
    }
}
