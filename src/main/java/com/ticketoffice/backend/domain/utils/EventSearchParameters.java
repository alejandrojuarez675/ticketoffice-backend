package com.ticketoffice.backend.domain.utils;

import com.google.common.base.Strings;
import com.ticketoffice.backend.domain.enums.EventStatus;
import com.ticketoffice.backend.domain.models.Event;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record EventSearchParameters(
        String country,
        String city,
        String query
) {

    public final static String ALL = "all";

    public Predicate<Event> getPredicate() {
        String title = Optional.ofNullable(query).map(String::toUpperCase).orElse("");

        return getPredicate(title).reduce(Predicate::and).orElse(event -> true);
    }

    private Stream<Predicate<Event>> getPredicate(String title) {
        return Stream.of(
                event -> event.location().country().equalsIgnoreCase(country) || isAll(country),
                event -> Strings.isNullOrEmpty(city) || event.location().city().equalsIgnoreCase(city) || isAll(city),
                event -> Strings.isNullOrEmpty(title) || event.title().toUpperCase().contains(title),
                event -> event.status().equals(EventStatus.ACTIVE),
                event -> event.date().isAfter(LocalDateTime.now())
        );
    }

    public boolean hasSpecificCity() {
        return !Strings.isNullOrEmpty(city) && !ALL.equalsIgnoreCase(city);
    }

    public boolean hasSpecificCountry() {
        return !Strings.isNullOrEmpty(country) && !ALL.equalsIgnoreCase(country);
    }

    private boolean isAll(String value) {
        return ALL.equalsIgnoreCase(value);
    }

}
