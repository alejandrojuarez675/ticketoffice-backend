package com.ticketoffice.backend.infra.adapters.in.dto.response.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountryDto {
    private final String code;
    private final String name;
    
    private CountryDto(Builder builder) {
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
        
        public CountryDto build() {
            return new CountryDto(this);
        }
    }
}
