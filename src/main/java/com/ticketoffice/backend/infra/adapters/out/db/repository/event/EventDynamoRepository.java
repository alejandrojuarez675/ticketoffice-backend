package com.ticketoffice.backend.infra.adapters.out.db.repository.event;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.enums.EventStatus;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.utils.EventSearchParameters;
import com.ticketoffice.backend.domain.utils.EventSimilarSearchParameters;
import com.ticketoffice.backend.infra.adapters.out.db.dao.EventDynamoDao;
import com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper;
import java.util.UUID;
import org.eclipse.jetty.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class EventDynamoRepository implements EventRepository {

    private final EventDynamoDao eventDao;

    @Inject
    public EventDynamoRepository(EventDynamoDao eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    public Optional<Event> getById(String id) {
        Map<String, AttributeValue> eventMap = eventDao.getById(id);
        return Optional.ofNullable(eventMap)
                .filter(map -> map.containsKey("id"))
                .map(EventDynamoDBMapper::fromMap);
    }

    @Override
    public Optional<Event> save(Event event) {
        String id = Optional.ofNullable(event.id()).orElse(UUID.randomUUID().toString());
        if (event.id() == null || event.id().isEmpty()) {
            event = event.getCopyWithUpdatedId(id);
        }
        eventDao.save(EventDynamoDBMapper.toMap(event));
        return Optional.of(event);
    }

    @Override
    public List<Event> findAll() {
        return eventDao.getAll()
                .stream()
                .map(EventDynamoDBMapper::fromMap)
                .toList();
    }

    @Override
    public Optional<Event> update(String id, Event event) {
        eventDao.update(id, EventDynamoDBMapper.toMap(event));
        return Optional.of(event);
    }

    @Override
    public Optional<Event> getByIdAndOrganizerId(String id, String id1) {
        return getById(id)
                .filter(event -> event.organizerId().equals(id1));
    }

    @Override
    public List<Event> findByUserId(String userId) {
        return eventDao.getEventsByOrganizer(userId)
                .stream()
                .map(EventDynamoDBMapper::fromMap)
                .toList();
    }

    @Override
    public Integer count(EventSearchParameters predicate) {
        return search(predicate).size();
    }

    @Override
    public List<Event> search(EventSearchParameters eventSearchParameters, Integer pageSize, Integer pageNumber) {
        List<Event> search = search(eventSearchParameters);
        return search.subList(pageNumber * pageSize, Math.min((pageNumber + 1) * pageSize, search.size()));
    }

    @Override
    public List<Event> search(EventSimilarSearchParameters parameters, Integer quantity) {

        List<Event> eventsSameSeller = getActiveEventsWithFilteredTheReferenceEvent(
                eventDao.getEventsByOrganizer(parameters.getEvent().organizerId()), parameters);

        List<Event> eventsSameSellerAndCity = eventsSameSeller.stream()
                .filter(e -> e.location().city().equals(parameters.getEvent().location().city()))
                .toList();

        Set<Event> result = new HashSet<>(eventsSameSellerAndCity);
        if (result.size() >= quantity) {
            return result.stream().toList().subList(0, quantity);
        }

        result.addAll(eventsSameSeller);
        if (result.size() >= quantity) {
            return result.stream().toList().subList(0, quantity);
        }

        List<Event> eventsSameCity = getActiveEventsWithFilteredTheReferenceEvent(
                eventDao.getEventsByCity(parameters.getEvent().location().city()), parameters);
        result.addAll(eventsSameCity);
        if (result.size() >= quantity) {
            return result.stream().toList().subList(0, quantity);
        }

        List<Event> eventsSameCountry = getActiveEventsWithFilteredTheReferenceEvent(
                eventDao.getEventsByCountry(parameters.getEvent().location().country()), parameters);
        result.addAll(eventsSameCountry);
        if (result.size() >= quantity) {
            return result.stream().toList().subList(0, quantity);
        }

        return result.stream().toList();
    }

    @NotNull
    private List<Event> getActiveEventsWithFilteredTheReferenceEvent(List<Map<String, AttributeValue>> eventDao, EventSimilarSearchParameters parameters) {
        return eventDao.stream()
                .map(EventDynamoDBMapper::fromMap)
                .filter(EventSimilarSearchParameters.getPredicateByActiveEvents())
                .filter(e -> !e.id().equals(parameters.getEvent().id()))
                .toList();
    }

    private List<Event> search(EventSearchParameters eventSearchParameters) {
        return getEventsByCountryOrAll(eventSearchParameters.country())
                .stream()
                .filter(e -> {
                    if (!StringUtil.isBlank(eventSearchParameters.city())) {
                        return e.location().city().equals(eventSearchParameters.city());
                    }
                    return true;
                })
                .filter(e -> {
                    if (!StringUtil.isBlank(eventSearchParameters.query())) {
                        return e.title().toUpperCase().contains(eventSearchParameters.query().toUpperCase());
                    }
                    return true;
                })
                .filter(e -> EventStatus.ACTIVE.equals(e.status()))
                .filter(e -> e.date().isAfter(LocalDateTime.now()))
                .toList();
    }

    private List<Event> getEventsByCountryOrAll(String country) {
        List<Event> eventsByCountry;
        if (!StringUtil.isBlank(country)) {
            eventsByCountry = eventDao.getEventsByCountry(country)
                    .stream()
                    .map(EventDynamoDBMapper::fromMap)
                    .toList();
        } else {
            eventsByCountry = findAll();
        }
        return eventsByCountry;
    }
}
