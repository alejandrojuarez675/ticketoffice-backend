package com.ticketoffice.backend.application.utils;

public class CheckoutSessionIdUtils {

    public static String createCheckoutSessionId(String eventId, String priceId) {
        return formatId(eventId, priceId) + "-" + System.currentTimeMillis();
    }

    public static String getCheckoutSessionPattern(String eventId, String priceId) {
        return formatId(eventId, priceId) + "-";
    }

    private static String formatId(String eventId, String priceId) {
        return eventId + "-" + priceId;
    }

    public static String getEventId(String checkoutSessionId) {
        return checkoutSessionId.split("-")[0];
    }

    public static String getPriceId(String checkoutSessionId) {
        return checkoutSessionId.split("-")[1];
    }
}
