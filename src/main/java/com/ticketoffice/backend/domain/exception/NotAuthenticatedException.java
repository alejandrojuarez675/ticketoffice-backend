package com.ticketoffice.backend.domain.exception;

public class NotAuthenticatedException extends Exception {
    public NotAuthenticatedException(String message) {
        super(message);
    }
}
