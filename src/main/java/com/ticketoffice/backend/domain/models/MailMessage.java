package com.ticketoffice.backend.domain.models;

import java.util.Map;
import java.util.Objects;

public class MailMessage {
    private final String templateName;
    private final String to;
    private final Map<String, String> templateData;

    public MailMessage(String templateName, String to, Map<String, String> templateData) {
        this.templateName = Objects.requireNonNull(templateName, "Template name cannot be null");
        this.to = Objects.requireNonNull(to, "Recipient email cannot be null");
        this.templateData = Map.copyOf(Objects.requireNonNull(templateData, "Template data cannot be null"));
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getTo() {
        return to;
    }

    public Map<String, String> getTemplateData() {
        return templateData;
    }
}
