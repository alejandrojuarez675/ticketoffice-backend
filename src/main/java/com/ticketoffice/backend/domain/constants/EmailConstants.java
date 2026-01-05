package com.ticketoffice.backend.domain.constants;

import java.util.List;

public class EmailConstants {
    public static final String FRONTEND_URL = "https://www.tuentradaya.com";
    public static final String PATH_TO_CONFIRMATION_PAGE = "/admin/events/{eventId}/validate?sale-id={saleId}";
    public static final String PATH_TO_RESET_PASSWORD = "/auth/reset-password?token=%s";
    public static final List<String> ADMIN_EMAIL = List.of("alejandrojuarez675@gmail.com", "adolfojhawork@gmail.com");
}
