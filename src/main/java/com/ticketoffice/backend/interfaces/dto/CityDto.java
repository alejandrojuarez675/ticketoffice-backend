package com.ticketoffice.backend.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CityDto {
    private final String code;
    private final String name;
    
    private CityDto(Builder builder) {
        this.code = builder.code;
        this.name = builder.name;
    }
    
    @JsonProperty("code")
    public String getCode() {
        return code;
    }
    
    @JsonProperty("name")
    public String getName() {
        return name;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String code;
        private String name;
        
        public Builder code(String code) {
            this.code = code;
            return this;
        }
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public CityDto build() {
            return new CityDto(this);
        }
    }
}
