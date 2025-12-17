package com.ticketoffice.backend.domain.models;

import com.ticketoffice.backend.domain.enums.MailTemplates;
import java.util.Map;
import java.util.Objects;

public class MailMessage {
    private final MailTemplates templateName;
    private final String to;
    private final Map<String, String> templateData;

    public MailMessage(MailTemplates templateName, String to, Map<String, String> templateData) {
        this.templateName = Objects.requireNonNull(templateName, "Template name cannot be null");
        this.to = Objects.requireNonNull(to, "Recipient email cannot be null");
        this.templateData = Map.copyOf(Objects.requireNonNull(templateData, "Template data cannot be null"));
    }

    public MailTemplates getTemplateName() {
        return templateName;
    }

    public String getTo() {
        return to;
    }

    public Map<String, String> getTemplateData() {
        return templateData;
    }
}
