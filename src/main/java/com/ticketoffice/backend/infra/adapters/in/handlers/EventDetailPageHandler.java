package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.models.TicketPrice;
import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetSimilarEventsToAnEventUseCase;
import com.ticketoffice.backend.domain.usecases.organizer.GetOrganizerByUserIdUseCase;
import com.ticketoffice.backend.domain.usecases.tickets.GetAvailableTicketStockIdUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventDetailPageResponseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventLightResponseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventDetailPageResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventLightResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.PriceDTO;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class EventDetailPageHandler {

    final private GetEventUseCase getEventUseCase;
    private final GetOrganizerByUserIdUseCase getOrganizerByUserIdUseCase;
    private final GetSimilarEventsToAnEventUseCase getSimilarEventsToAnEventUseCase;
    private final GetAvailableTicketStockIdUseCase getAvailableTicketStockIdUseCase;

    public EventDetailPageHandler(
            GetEventUseCase getEventUseCase,
            GetOrganizerByUserIdUseCase getOrganizerByUserIdUseCase,
            GetSimilarEventsToAnEventUseCase getSimilarEventsToAnEventUseCase,
            GetAvailableTicketStockIdUseCase getAvailableTicketStockIdUseCase
    ) {
        this.getEventUseCase = getEventUseCase;
        this.getOrganizerByUserIdUseCase = getOrganizerByUserIdUseCase;
        this.getSimilarEventsToAnEventUseCase = getSimilarEventsToAnEventUseCase;
        this.getAvailableTicketStockIdUseCase = getAvailableTicketStockIdUseCase;
    }

    public EventDetailPageResponse getEvent(String id) throws NotFoundException {
        Event event = getEventUseCase.apply(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id %s not found", id)));

        Organizer organizer = getOrganizerByUserIdUseCase.apply(event.organizerId())
                .orElse(new Organizer(event.organizerId(), null, null, null));

        List<PriceDTO> priceListToOverride = event.prices().stream()
                .map(getTicketPricePriceDTOFunction(event)).toList();

        EventDetailPageResponse response = EventDetailPageResponseMapper.toResponse(event, organizer, priceListToOverride);

        return new EventDetailPageResponse(
                response.id(),
                response.title(),
                response.date(),
                response.location(),
                response.image(),
                priceListToOverride,
                response.description(),
                response.additionalInfo(),
                response.organizer(),
                response.status()
        );
    }

    private Function<TicketPrice, PriceDTO> getTicketPricePriceDTOFunction(Event event) {
        return price -> {
            try {
                Integer availableTicketStock = getAvailableTicketStockIdUseCase.apply(event, price.id());
                return new PriceDTO(price.id(), price.value(), price.currency(), price.type(), price.isFree(), availableTicketStock);
            } catch (ResourceDoesntExistException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public List<EventLightResponse> getRecommendationByEvent(String id) throws NotFoundException {
        try {
            return getSimilarEventsToAnEventUseCase.apply(id, 5)
                    .stream()
                    .map(EventLightResponseMapper::getFromEvent)
                    .toList();
        } catch (ResourceDoesntExistException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
