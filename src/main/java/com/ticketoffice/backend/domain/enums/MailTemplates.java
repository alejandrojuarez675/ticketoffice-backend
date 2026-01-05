package com.ticketoffice.backend.domain.enums;

public enum MailTemplates {
    CONFIRMATION_EMAIL_TEMPLATE("confirm-email-template"),
    SEND_TICKET("send-ticket"),
    WELCOME_USER("welcome-user"),
    FORGOT_PASSWORD("forgot-password"),
    PASSWORD_UPDATED("password-updated"),
    NOTIFICATION_TO_ADMIN("notification-to-admin");

    private final String template;

    MailTemplates(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
