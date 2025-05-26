package com.ticketoffice.backend.domain.exception;

public class ErrorOnPersistDataException extends Exception {
    public ErrorOnPersistDataException(String message) {
        super(message);
    }
}
