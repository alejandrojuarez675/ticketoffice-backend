package com.ticketoffice.backend.infra.adapters.in.dto.request;

import java.util.List;

public record BuyTicketsRequest(
        String mainEmail,
        List<PersonalData> buyer
) {
    public record PersonalData(
            String name,
            String lastName,
            String email,
            String phone,
            String nationality,
            String documentType,
            String document
    ){}
}
