package com.ticketoffice.backend.domain.models;

import java.time.LocalDateTime;

public class CheckoutSession {

    private String id;
    private String eventId;
    private String priceId;
    private Integer quantity;
    private Double price;
    private Status status;
    private LocalDateTime expirationTime;

    public CheckoutSession(String id, String eventId, String priceId, Integer quantity, Double price, Status status, LocalDateTime expirationTime) {
        this.id = id;
        this.eventId = eventId;
        this.priceId = priceId;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CheckoutSession getCopyWithUpdatedStatus(Status status) {
        return new CheckoutSession(
                this.getId(),
                this.getEventId(),
                this.getPriceId(),
                this.getQuantity(),
                this.getPrice(),
                status,
                this.getExpirationTime());
    }

    public enum Status {CREATED, CONFIRMING}
}
