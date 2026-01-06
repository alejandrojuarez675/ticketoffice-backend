package com.ticketoffice.backend.infra.adapters.in.dto.response.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyDto {
    private final String code;
    private final String name;
    private final String symbol;
    
    @JsonCreator
    public CurrencyDto(
            @JsonProperty("code") String code,
            @JsonProperty("name") String name,
            @JsonProperty("symbol") String symbol) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
    }
    
    private CurrencyDto(Builder builder) {
        this.code = builder.code;
        this.name = builder.name;
        this.symbol = builder.symbol;
    }
    
    @JsonProperty("code")
    public String getCode() {
        return code;
    }
    
    @JsonProperty("name")
    public String getName() {
        return name;
    }
    
    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String code;
        private String name;
        private String symbol;
        
        public Builder code(String code) {
            this.code = code;
            return this;
        }
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }
        
        public CurrencyDto build() {
            return new CurrencyDto(this);
        }
    }
}
