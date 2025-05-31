package com.ticketoffice.backend.domain.models;

import java.time.LocalDateTime;

public class CheckoutSession {

    private String id;
    private String eventId;
    private String priceId;
    private Integer quantity;
    private LocalDateTime expirationTime;

    public CheckoutSession(String id, String eventId, String priceId, Integer quantity, LocalDateTime expirationTime) {
        this.id = id;
        this.eventId = eventId;
        this.priceId = priceId;
        this.quantity = quantity;
        this.expirationTime = expirationTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public boolean isExpired() {
        return expirationTime.isBefore(LocalDateTime.now());
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }
}
