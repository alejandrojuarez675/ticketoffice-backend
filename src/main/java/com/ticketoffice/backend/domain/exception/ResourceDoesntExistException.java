package com.ticketoffice.backend.domain.exception;

public class ResourceDoesntExistException extends Exception {
    public ResourceDoesntExistException(String message) {
        super(message);
    }
}
