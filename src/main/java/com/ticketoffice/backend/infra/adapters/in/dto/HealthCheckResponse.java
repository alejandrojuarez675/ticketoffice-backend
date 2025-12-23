package com.ticketoffice.backend.infra.adapters.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Health check response containing the status and timestamp of the service")
public class HealthCheckResponse {
    
    @Schema(description = "Service status", example = "healthy")
    private String status;
    
    @Schema(description = "Timestamp of the health check in ISO-8601 format", 
            example = "2025-12-23T18:14:30.123456789Z")
    private String timestamp;

    public HealthCheckResponse(String status, String timestamp) {
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters
    public String getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
