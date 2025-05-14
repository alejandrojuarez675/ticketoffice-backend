package com.ticketoffice.backend.infra.adapters.in.exception;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
