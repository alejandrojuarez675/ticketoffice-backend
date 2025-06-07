package com.ticketoffice.backend.infra.adapters.in.dto.request.validators;

import com.ticketoffice.backend.infra.adapters.in.dto.request.EventCrudRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.ImageDTO;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.LocationDTO;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.TicketDTO;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import java.util.List;

public class EventCrudRequestValidator implements RequestValidator<EventCrudRequest> {
    @Override
    public void validate(EventCrudRequest request) throws BadRequestException {
        if (request == null) {
            throw new BadRequestException("Request is required");
        }
        if (request.title() == null || request.title().isBlank()) {
            throw new BadRequestException("Title is required");
        }
        if (request.description() == null || request.description().isBlank()) {
            throw new BadRequestException("Description is required");
        }
        if (request.date() == null) {
            throw new BadRequestException("Date is required");
        }

        validate(request.location());
        validate(request.image());
        validate(request.tickets());

    }

    private void validate(List<TicketDTO> prices) throws BadRequestException {
        if (prices == null || prices.isEmpty()) {
            throw new BadRequestException("Prices are required");
        }
        for (TicketDTO price : prices) {
            if (price == null) {
                throw new BadRequestException("Price is required");
            }
            if (price.value() == null) {
                throw new BadRequestException("Price value is required");
            }
            if (price.currency() == null || price.currency().isBlank()) {
                throw new BadRequestException("Price currency is required");
            }
            if (price.type() == null || price.type().isBlank()) {
                throw new BadRequestException("Price type is required");
            }
        }
    }

    private void validate(ImageDTO image) throws BadRequestException {
        if (image == null) {
            throw new BadRequestException("Image is required");
        }
        if (image.url() == null || image.url().isBlank()) {
            throw new BadRequestException("Image URL is required");
        }
        if (image.alt() == null || image.alt().isBlank()) {
            throw new BadRequestException("Image alt text is required");
        }
    }

    private void validate(LocationDTO location) throws BadRequestException {
        if (location == null) {
            throw new BadRequestException("Location is required");
        }
        if (location.name() == null || location.name().isBlank()) {
            throw new BadRequestException("Location name is required");
        }
        if (location.address() == null || location.address().isBlank()) {
            throw new BadRequestException("Location address is required");
        }
        if (location.city() == null || location.city().isBlank()) {
            throw new BadRequestException("Location city is required");
        }
    }
}
