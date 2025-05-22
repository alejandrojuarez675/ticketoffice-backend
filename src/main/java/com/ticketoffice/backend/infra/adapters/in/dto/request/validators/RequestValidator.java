package com.ticketoffice.backend.infra.adapters.in.dto.request.validators;

import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;

public interface RequestValidator <T> {
    void validate(T request) throws BadRequestException;
}
