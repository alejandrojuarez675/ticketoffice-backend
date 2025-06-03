package com.ticketoffice.backend.infra.adapters.in.dto.request.validators;

import com.ticketoffice.backend.infra.adapters.in.dto.request.BuyTicketsRequest;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;

public class BuyTicketsRequestValidator implements RequestValidator<BuyTicketsRequest> {
    @Override
    public void validate(BuyTicketsRequest request) throws BadRequestException {
        if (request == null) {
            throw new BadRequestException("Request is required");
        }
        if (StringUtils.isBlank(request.sessionId())) {
            throw new BadRequestException("Session id is required");
        }
        if (request.buyer() == null || request.buyer().isEmpty()) {
            throw new BadRequestException("Buyer is required");
        }
        if (StringUtils.isBlank(request.mainEmail())) {
            throw new BadRequestException("Main email is required");
        }
        for (BuyTicketsRequest.PersonalData personalData : request.buyer()) {
            validate(personalData);
        }
    }

    private void validate(BuyTicketsRequest.PersonalData personalData) throws BadRequestException {
        if (personalData == null) {
            throw new BadRequestException("Personal data is required");
        }
        if (StringUtils.isBlank(personalData.name())) {
            throw new BadRequestException("First name is required");
        }
        if (StringUtils.isBlank(personalData.lastName())) {
            throw new BadRequestException("Last name is required");
        }
        if (StringUtils.isBlank(personalData.email())) {
            throw new BadRequestException("Email is required");
        }
    }
}
