package com.ticketoffice.backend.infra.adapters.in.dto.request.validators;

import com.google.common.base.Strings;
import com.ticketoffice.backend.infra.adapters.in.dto.request.BuyTicketsRequest;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;

public class BuyTicketsRequestValidator implements RequestValidator<BuyTicketsRequest> {
    @Override
    public void validate(BuyTicketsRequest request) throws BadRequestException {
        if (request == null) {
            throw new BadRequestException("Request is required");
        }
        if (request.buyer() == null || request.buyer().isEmpty()) {
            throw new BadRequestException("Buyer is required");
        }
        if (Strings.isNullOrEmpty(request.mainEmail())) {
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
        if (Strings.isNullOrEmpty(personalData.name())) {
            throw new BadRequestException("First name is required");
        }
        if (Strings.isNullOrEmpty(personalData.lastName())) {
            throw new BadRequestException("Last name is required");
        }
        if (Strings.isNullOrEmpty(personalData.email())) {
            throw new BadRequestException("Email is required");
        }
    }
}
