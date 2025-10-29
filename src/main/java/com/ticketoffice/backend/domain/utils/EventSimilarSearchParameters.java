package com.ticketoffice.backend.domain.utils;

import com.ticketoffice.backend.domain.enums.EventStatus;
import com.ticketoffice.backend.domain.models.Event;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EventSimilarSearchParameters{

    private final Event event;

    public EventSimilarSearchParameters(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public static EventSimilarSearchParameters of(Event event) {
        return new EventSimilarSearchParameters(event);
    }

    public List<List<Predicate<Event>>> getPredicates() {
        return List.of(
                getPredicatesBySameSellerAndCity(event),
                getPredicatesBySameSeller(event),
                getPredicatesBySameCity(event),
                getPredicatesBySameCountry(event)
        );
    }

    public static List<Predicate<Event>> getPredicateForPreventDuplicates(List<Event> results) {
        return results.stream()
                .map(Event::id)
                .map(id -> (Predicate<Event>) eventPred -> !eventPred.id().equals(id))
                .toList();
    }

    private static List<Predicate<Event>> getPredicatesBySameCity(Event eventReference) {
        List<Predicate<Event>> predicates = new ArrayList<>(getPredicatesBySameCountry(eventReference));
        predicates.add(event -> event.location().city().equals(eventReference.location().city()));
        return predicates;
    }

    private static List<Predicate<Event>> getPredicatesBySameCountry(Event eventReference) {
        List<Predicate<Event>> predicates = new ArrayList<>(getPredicatesByAnotherActiveEvents(eventReference));
        predicates.add(event -> event.location().country().equals(eventReference.location().country()));
        return predicates;
    }

    private static List<Predicate<Event>> getPredicatesBySameSellerAndCity(Event eventReference) {
        List<Predicate<Event>> predicates = new ArrayList<>(getPredicatesBySameSeller(eventReference));
        predicates.add(event -> event.location().country().equals(eventReference.location().country()));
        predicates.add(event -> event.location().city().equals(eventReference.location().city()));
        return predicates;
    }

    private static List<Predicate<Event>> getPredicatesBySameSeller(Event eventReference) {
        List<Predicate<Event>> predicates = new ArrayList<>(getPredicatesByAnotherActiveEvents(eventReference));
        predicates.add(event -> event.organizerId().equals(eventReference.organizerId()));
        return predicates;
    }

    private static List<Predicate<Event>> getPredicatesByAnotherActiveEvents(Event eventReference) {
        List<Predicate<Event>> predicates = new ArrayList<>(getPredicatesByActiveEvents());
        predicates.add(event -> !event.id().equals(eventReference.id()));
        return predicates;
    }

    public static List<Predicate<Event>> getPredicatesByActiveEvents() {
        return List.of(
                event -> event.date().isAfter(LocalDateTime.now()),
                event -> event.status().equals(EventStatus.ACTIVE)
        );
    }

    public static Predicate<Event> getPredicateByActiveEvents() {
        return getPredicatesByActiveEvents().stream()
                .reduce(Predicate::and)
                .orElse(e -> true);
    }
}
