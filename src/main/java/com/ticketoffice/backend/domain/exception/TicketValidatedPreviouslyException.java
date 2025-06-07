package com.ticketoffice.backend.domain.exception;

public class TicketValidatedPreviouslyException extends Exception {
    public TicketValidatedPreviouslyException(String message) {
        super(message);
    }
}
