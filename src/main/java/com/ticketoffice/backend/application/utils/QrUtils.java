package com.ticketoffice.backend.application.utils;

public class QrUtils {
    public static String generateSvgOfQrCode(String data) {
        return """
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
                    <image href="https://api.qrserver.com/v1/create-qr-code/?data=%s" x="0" y="0" width="100" height="100" />
                </svg>
                """.formatted(data);
    }
}
