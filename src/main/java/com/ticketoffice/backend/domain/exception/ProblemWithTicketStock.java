package com.ticketoffice.backend.domain.exception;

public class ProblemWithTicketStock extends Exception {
    public ProblemWithTicketStock(String message) {
        super(message);
    }
}
