package com.ticketoffice.backend.infra.adapters.in.dto.request.validators;

import com.ticketoffice.backend.infra.adapters.in.dto.request.CreateSessionRequest;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;

public class CreateSessionRequestValidator implements RequestValidator<CreateSessionRequest> {

    public static final Integer MAX_QUANTITY_TO_BUY_AT_SAME_TIME = 5;

    @Override
    public void validate(CreateSessionRequest request) throws BadRequestException {
        if (request == null) {
            throw new BadRequestException("Request is required");
        }
        if (StringUtils.isBlank(request.eventId())) {
            throw new BadRequestException("Event id is required");
        }
        if (StringUtils.isBlank(request.priceId())) {
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
