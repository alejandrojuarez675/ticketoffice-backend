package com.ticketoffice.backend.infra.adapters.in.dto.request.validators;

import com.google.common.base.Strings;
import com.ticketoffice.backend.infra.adapters.in.dto.request.CreateSessionRequest;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;

public class CreateSessionRequestValidator implements RequestValidator<CreateSessionRequest> {

    public static final Integer MAX_QUANTITY_TO_BUY_AT_SAME_TIME = 5;

    @Override
    public void validate(CreateSessionRequest request) throws BadRequestException {
        if (request == null) {
            throw new BadRequestException("Request is required");
        }
        if (Strings.isNullOrEmpty(request.eventId())) {
            throw new BadRequestException("Event id is required");
        }
        if (Strings.isNullOrEmpty(request.priceId())) {
            throw new BadRequestException("Price id is required");
        }
        if (request.quantity() == null) {
            throw new BadRequestException("Quantity is required");
        }
        if (request.quantity() > MAX_QUANTITY_TO_BUY_AT_SAME_TIME) {
            throw new BadRequestException("Quantity must be less than " + MAX_QUANTITY_TO_BUY_AT_SAME_TIME);
        }
    }
}
