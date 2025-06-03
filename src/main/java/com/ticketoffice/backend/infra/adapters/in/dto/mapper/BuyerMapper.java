package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.Buyer;
import com.ticketoffice.backend.infra.adapters.in.dto.request.BuyTicketsRequest;

public class BuyerMapper {
    public static Buyer get(BuyTicketsRequest.PersonalData personalData) {
        return new Buyer(
                null,
                personalData.name(),
                personalData.lastName(),
                personalData.email(),
                personalData.phone(),
                personalData.nationality(),
                personalData.documentType(),
                personalData.document()
        );
    }
}
