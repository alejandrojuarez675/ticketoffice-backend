package com.ticketoffice.backend.infra.adapters.in.dto.response;

import java.util.List;

public record CongratsCheckout(
        List<String> qrCodes
) {
}
