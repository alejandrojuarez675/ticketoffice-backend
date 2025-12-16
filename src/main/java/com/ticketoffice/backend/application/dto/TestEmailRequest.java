package com.ticketoffice.backend.application.dto;

import com.ticketoffice.backend.domain.enums.MailTemplates;
import java.util.Map;

public record TestEmailRequest(
    MailTemplates templateName,
    String to,
    Map<String, String> templateData
) {}
