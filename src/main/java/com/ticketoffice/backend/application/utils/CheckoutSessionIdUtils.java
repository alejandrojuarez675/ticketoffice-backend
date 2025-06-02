package com.ticketoffice.backend.application.utils;

public class CheckoutSessionIdUtils {

    public static final String SEPARATOR = "__";

    public static String createCheckoutSessionId(String eventId, String priceId, Integer quantity) {
        return formatId(eventId, priceId, quantity) + SEPARATOR + System.currentTimeMillis();
    }

    public static String getCheckoutSessionPattern(String eventId, String priceId) {
        return silentPattern(eventId + SEPARATOR + priceId + SEPARATOR) + ".*";
    }

    private static String silentPattern(String s) {
        return s.replace("_", "\\_");
    }

    private static String formatId(String eventId, String priceId, Integer quantity) {
        return eventId + SEPARATOR + priceId + SEPARATOR + quantity;
    }

    public static String getEventId(String checkoutSessionId) {
        return checkoutSessionId.split(SEPARATOR)[0];
    }

    public static String getPriceId(String checkoutSessionId) {
        return checkoutSessionId.split(SEPARATOR)[1];
    }

    public static Integer getQuantity(String checkoutSessionId) {
        return Integer.valueOf(checkoutSessionId.split(SEPARATOR)[2]);
    }
}
