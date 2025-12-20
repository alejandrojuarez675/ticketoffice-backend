package com.ticketoffice.backend.infra.adapters.out.cache.dynamo;

import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.ports.CheckoutSessionCache;
import com.ticketoffice.backend.infra.adapters.out.cache.dynamo.dao.CheckoutSessionDynamoDao;
import com.ticketoffice.backend.infra.adapters.out.cache.dynamo.mapper.CheckoutSessionDynamoDBMapper;
import jakarta.inject.Inject;
import java.util.Map;

import java.util.Optional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class CheckoutSessionDynamoRepository implements CheckoutSessionCache {

    private final CheckoutSessionDynamoDao dao;

    @Inject
    public CheckoutSessionDynamoRepository(CheckoutSessionDynamoDao dao) {
        this.dao = dao;
    }

    @Override
    public Integer countByEventIdAndTicketId(String eventId, String ticketId) {
        return dao.countByEventIdAndTicketId(eventId, ticketId);
    }

    @Override
    public Optional<CheckoutSession> getById(String sessionId) {
        return Optional.ofNullable(dao.getById(sessionId))
                .map(CheckoutSessionDynamoDBMapper::fromMap);
    }

    @Override
    public Optional<CheckoutSession> createCheckoutSession(CheckoutSession checkoutSession) {
        Map<String, AttributeValue> valueMap = CheckoutSessionDynamoDBMapper.toMap(checkoutSession);
        dao.save(valueMap);
        return getById(checkoutSession.getId());
    }

    @Override
    public void deleteById(String sessionId) {
        dao.deleteById(sessionId);
    }

    @Override
    public Optional<CheckoutSession> updateStatus(String sessionId, CheckoutSession.Status status) {
        return getById(sessionId)
                .map(session -> {
                    CheckoutSession updatedSession = session.getCopyWithUpdatedStatus(status);
                    Map<String, AttributeValue> valueMap = CheckoutSessionDynamoDBMapper.toMap(updatedSession);
                    dao.save(valueMap);
                    return updatedSession;
                });
    }
}
