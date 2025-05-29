package com.ticketoffice.backend.application.usecases.events;

import com.ticketoffice.backend.domain.enums.EventStatus;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.usecases.events.GetEventsByParamsUseCase;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;

@Service
public class GetEventsByParamsUseCaseImpl implements GetEventsByParamsUseCase {

    private final EventRepository eventRepository;

    public GetEventsByParamsUseCaseImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> getEventsByParams(String city, String query, Integer pageSize, Integer pageNumber) {
        String title = Optional.ofNullable(query).map(String::toUpperCase).orElse("");

        List<Predicate<Event>> predicates = List.of(
                event -> event.location().city().equals(city),
                event -> event.title().toUpperCase().contains(title),
                event -> event.status().equals(EventStatus.ACTIVE)
        );

        return eventRepository.search(predicates, pageSize, pageNumber);
    }
}
