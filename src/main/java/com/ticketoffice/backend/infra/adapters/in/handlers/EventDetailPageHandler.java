package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetSimilarEventsToAnEventUseCase;
import com.ticketoffice.backend.domain.usecases.organizer.GetOrganizerByUserIdUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventDetailPageResponseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventLightResponseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventDetailPageResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventLightResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventDetailPageHandler {

    final private GetEventUseCase getEventUseCase;
    private final GetOrganizerByUserIdUseCase getOrganizerByUserIdUseCase;
    private final GetSimilarEventsToAnEventUseCase getSimilarEventsToAnEventUseCase;

    public EventDetailPageHandler(
            GetEventUseCase getEventUseCase,
            GetOrganizerByUserIdUseCase getOrganizerByUserIdUseCase,
            GetSimilarEventsToAnEventUseCase getSimilarEventsToAnEventUseCase
    ) {
        this.getEventUseCase = getEventUseCase;
        this.getOrganizerByUserIdUseCase = getOrganizerByUserIdUseCase;
        this.getSimilarEventsToAnEventUseCase = getSimilarEventsToAnEventUseCase;
    }

    public EventDetailPageResponse getEvent(String id) throws NotFoundException {
        Event event = getEventUseCase.getEventById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id %s not found", id)));

        Organizer organizer = getOrganizerByUserIdUseCase.findByUserId(event.organizerId())
                .orElse(new Organizer(event.organizerId(), null, null, null));

        return EventDetailPageResponseMapper.toResponse(event, organizer);
    }

    public List<EventLightResponse> getRecommendationByEvent(String id) throws NotFoundException {
        try {
            return getSimilarEventsToAnEventUseCase.getSimilarEventsToAnEvent(id, 5)
                    .stream()
                    .map(EventLightResponseMapper::getFromEvent)
                    .toList();
        } catch (ResourceDoesntExistException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
