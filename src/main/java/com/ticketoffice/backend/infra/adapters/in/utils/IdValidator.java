package com.ticketoffice.backend.infra.adapters.in.utils;

import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;

public class IdValidator {

    public static final String UUID_REGEX = "^[a-f\\d]{8}(-[a-f\\d]{4}){3}-[a-f\\d]{12}$";

    public static void validateIdFromParams(String id, String name, Boolean required) throws BadRequestException {
        if (required && id == null) {
            throw new BadRequestException(String.format("%s is required", name));
        }

        if (id != null) {
            if (!id.matches(UUID_REGEX)) {
                throw new BadRequestException(String.format("%s is not valid", name));
            }
        }
    }
}
