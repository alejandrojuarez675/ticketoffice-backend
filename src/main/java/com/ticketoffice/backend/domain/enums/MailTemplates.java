package com.ticketoffice.backend.domain.enums;

public enum MailTemplates {
    CONFIRMATION_EMAIL_TEMPLATE("confirm-email-template"),
    SEND_TICKET("send-ticket"),
    WELCOME_USER("welcome-user"),
    FORGOT_PASSWORD("forgot-password");

    private final String template;

    MailTemplates(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
