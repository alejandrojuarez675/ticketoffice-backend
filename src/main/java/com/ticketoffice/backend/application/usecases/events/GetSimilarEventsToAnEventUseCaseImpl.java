package com.ticketoffice.backend.application.usecases.events;

import com.ticketoffice.backend.domain.enums.EventStatus;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetSimilarEventsToAnEventUseCase;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;

@Service
public class GetSimilarEventsToAnEventUseCaseImpl implements GetSimilarEventsToAnEventUseCase {

    private final GetEventUseCase getEventUseCase;
    private final EventRepository eventRepository;

    public GetSimilarEventsToAnEventUseCaseImpl(
            GetEventUseCase getEventUseCase,
            EventRepository eventRepository
    ) {
        this.getEventUseCase = getEventUseCase;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> getSimilarEventsToAnEvent(String eventId, Integer quantity) throws ResourceDoesntExistException {
        Event event = getEventUseCase.getEventById(eventId)
                .orElseThrow(() -> new ResourceDoesntExistException(String.format("Event with id %s not found", eventId)));

        List<List<Predicate<Event>>> predicates = new ArrayList<>(List.of(
                getPredicatesBySameSellerAndCity(event),
                getPredicatesBySameSeller(event),
                getPredicatesBySameCity(event),
                getPredicatesBySameCountry(event)
        ));

        List<Event> results = new ArrayList<>();
        predicates.forEach(predicate -> {
            if (results.size() < quantity) {
                ArrayList<Predicate<Event>> completePredicates = new ArrayList<>(predicate);
                completePredicates.addAll(getPredicateForPreventDuplicates(results));
                results.addAll(eventRepository.search(completePredicates, quantity - results.size(), 0));
            }
        });

        return results;
    }

    private static List<Predicate<Event>> getPredicateForPreventDuplicates(List<Event> results) {
        return results.stream()
                .map(Event::id)
                .map(id -> (Predicate<Event>) eventPred -> !eventPred.id().equals(id))
                .toList();
    }

    private List<Predicate<Event>> getPredicatesBySameCity(Event eventReference) {
        List<Predicate<Event>> predicates = new ArrayList<>(getPredicatesBySameCountry(eventReference));
        predicates.add(event -> event.location().city().equals(eventReference.location().city()));
        return predicates;
    }

    private List<Predicate<Event>> getPredicatesBySameCountry(Event eventReference) {
        List<Predicate<Event>> predicates = new ArrayList<>(getPredicatesByAnotherActiveEvents(eventReference));
        predicates.add(event -> event.location().country().equals(eventReference.location().country()));
        return predicates;
    }

    private List<Predicate<Event>> getPredicatesBySameSellerAndCity(Event eventReference) {
        List<Predicate<Event>> predicates = new ArrayList<>(getPredicatesBySameSeller(eventReference));
        predicates.add(event -> event.location().country().equals(eventReference.location().country()));
        predicates.add(event -> event.location().city().equals(eventReference.location().city()));
        return predicates;
    }

    private List<Predicate<Event>> getPredicatesBySameSeller(Event eventReference) {
        List<Predicate<Event>> predicates = new ArrayList<>(getPredicatesByAnotherActiveEvents(eventReference));
        predicates.add(event -> event.organizerId().equals(eventReference.organizerId()));
        return predicates;
    }

    private List<Predicate<Event>> getPredicatesByAnotherActiveEvents(Event eventReference) {
        List<Predicate<Event>> predicates = new ArrayList<>(getPredicatesByActiveEvents());
        predicates.add(event -> !event.id().equals(eventReference.id()));
        return predicates;
    }

    private List<Predicate<Event>> getPredicatesByActiveEvents() {
        return List.of(
                event -> event.date().isAfter(LocalDateTime.now()),
                event -> event.status().equals(EventStatus.ACTIVE)
        );
    }
}
