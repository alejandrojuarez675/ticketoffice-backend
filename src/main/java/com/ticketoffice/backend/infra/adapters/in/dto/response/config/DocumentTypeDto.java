package com.ticketoffice.backend.infra.adapters.in.dto.response.config;

public record DocumentTypeDto(
    String code,
    String name,
    String description,
    String format,
    String regex
) {
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
            return new DocumentTypeDto(code, name, description, format, regex);
        }
    }
}
