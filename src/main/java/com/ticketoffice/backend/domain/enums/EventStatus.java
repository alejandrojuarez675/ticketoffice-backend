package com.ticketoffice.backend.domain.enums;

public enum EventStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),;

    private final String status;

    EventStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
