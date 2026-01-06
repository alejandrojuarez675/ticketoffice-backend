package com.ticketoffice.backend.infra.adapters.in.dto.response.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentTypeDto {
    private final String code;
    private final String name;
    private final String description;
    private final String format;
    private final String regex;

    @JsonCreator
    public static DocumentTypeDto create(
            @JsonProperty("code") String code,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("format") String format,
            @JsonProperty("regex") String regex) {
        return builder()
                .code(code)
                .name(name)
                .description(description)
                .format(format)
                .regex(regex)
                .build();
    }

    private DocumentTypeDto(Builder builder) {
        this.code = builder.code;
        this.name = builder.name;
        this.description = builder.description;
        this.format = builder.format;
        this.regex = builder.regex;
    }
    
    @JsonProperty("code")
    public String getCode() {
        return code;
    }
    
    @JsonProperty("name")
    public String getName() {
        return name;
    }
    
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }
    
    @JsonProperty("format")
    public String getFormat() {
        return format;
    }
    
    @JsonProperty("regex")
    public String getRegex() {
        return regex;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String code;
        private String name;
        private String description;
        private String format;
        private String regex;
        
        public Builder code(String code) {
            this.code = code;
            return this;
        }
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder format(String format) {
            this.format = format;
            return this;
        }
        
        public Builder regex(String regex) {
            this.regex = regex;
            return this;
        }
        
        public DocumentTypeDto build() {
            return new DocumentTypeDto(this);
        }
    }
}
