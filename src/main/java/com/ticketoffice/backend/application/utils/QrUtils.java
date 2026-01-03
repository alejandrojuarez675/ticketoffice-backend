package com.ticketoffice.backend.application.utils;

import com.ticketoffice.backend.domain.constants.EmailConstants;

public class QrUtils {
    public static String generateSvgOfQrCode(String data) {
        return """
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
                    <image href="https://api.qrserver.com/v1/create-qr-code/?data=%s" x="0" y="0" width="100" height="100" />
                </svg>
                """.formatted(data);
    }

    public static String generateSvgOfQrCodeWithTicket(String eventId, String saleId) {
        return generateSvgOfQrCode(getUrlToConfirmTicket(eventId, saleId));
    }

    public static String getUrlToConfirmTicket(String eventId, String saleId) {
        return EmailConstants.FRONTEND_URL
                + EmailConstants.PATH_TO_CONFIRMATION_PAGE.replace("{eventId}", eventId).replace("{saleId}", saleId);
    }
}
