package com.ticketoffice.backend.infra.adapters.in.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record BuyTicketsRequest(

        @Schema(description = "Main email of the buyer", example = "example@gmail")
        String mainEmail,

        @Schema(description = "The list of buyers. You have to have one per ticket, the first have to be the main buyer")
        List<PersonalData> buyer
) {

    public record PersonalData(

            @Schema(description = "The name of the buyer", example = "John")
            String name,

            @Schema(description = "The last name of the buyer", example = "Doe")
            String lastName,

            @Schema(description = "The email of the buyer", example = "example@gmail")
            String email,

            @Schema(description = "The phone of the buyer", example = "+573012345678")
            String phone,

            @Schema(description = "The nationality of the buyer", example = "Colombia")
            String nationality,

            @Schema(description = "The type of the document of the buyer", example = "CC")
            String documentType,

            @Schema(description = "The document of the buyer", example = "123456789")
            String document
    ){}
}
