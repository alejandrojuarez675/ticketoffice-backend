package com.ticketoffice.backend.domain.utils;

import com.ticketoffice.backend.domain.enums.EventStatus;
import com.ticketoffice.backend.domain.models.Event;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;

public record EventSearchParameters(
        String country,
        String city,
        String query
) {

    public List<Predicate<Event>> getPredicates() {
        String title = Optional.ofNullable(query).map(String::toUpperCase).orElse(StringUtils.EMPTY);

        return List.of(
                event -> event.location().country().equals(country),
                event -> StringUtils.isEmpty(city) || event.location().city().equals(city),
                event -> StringUtils.isEmpty(title) || event.title().toUpperCase().contains(title),
                event -> event.status().equals(EventStatus.ACTIVE),
                event -> event.date().isAfter(LocalDateTime.now())
        );
    }

}
