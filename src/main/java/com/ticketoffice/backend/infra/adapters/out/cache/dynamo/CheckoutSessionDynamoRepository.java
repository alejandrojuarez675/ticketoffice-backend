package com.ticketoffice.backend.infra.adapters.out.cache.dynamo;

import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.ports.CheckoutSessionCache;
import com.ticketoffice.backend.infra.adapters.out.cache.dynamo.dao.CheckoutSessionDynamoDao;
import com.ticketoffice.backend.infra.adapters.out.cache.dynamo.mapper.CheckoutSessionDynamoDBMapper;
import com.ticketoffice.backend.infra.adapters.out.db.utils.RetryUtil;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.Optional;

public class CheckoutSessionDynamoRepository implements CheckoutSessionCache {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutSessionDynamoRepository.class);
    private static final RetryUtil.RetryConfig RETRY_CONFIG = new RetryUtil.RetryConfig();

    private final CheckoutSessionDynamoDao dao;

    @Inject
    public CheckoutSessionDynamoRepository(CheckoutSessionDynamoDao dao) {
        this.dao = dao;
    }

    @Override
    public Integer countByEventIdAndTicketId(String eventId, String ticketId) {
        try {
            return RetryUtil.executeWithRetry(
                "countByEventIdAndTicketId",
                () -> dao.countByEventIdAndTicketId(eventId, ticketId),
                0,
                RETRY_CONFIG
            );
        } catch (Exception e) {
            logger.error("Error counting checkout sessions by eventId: {} and ticketId: {} - {}", 
                eventId, ticketId, e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public Optional<CheckoutSession> getById(String sessionId) {
        try {
            Map<String, AttributeValue> item = RetryUtil.executeWithRetry(
                "getById",
                () -> dao.getById(sessionId),
                null,
                RETRY_CONFIG
            );
            
            return Optional.ofNullable(item)
                .map(CheckoutSessionDynamoDBMapper::fromMap);
        } catch (Exception e) {
            logger.error("Error getting checkout session by id: {} - {}", sessionId, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<CheckoutSession> createCheckoutSession(CheckoutSession checkoutSession) {
        try {
            Map<String, AttributeValue> valueMap = CheckoutSessionDynamoDBMapper.toMap(checkoutSession);
            RetryUtil.executeWithRetry(
                "saveCheckoutSession",
                () -> dao.save(valueMap),
                RETRY_CONFIG
            );
            return getById(checkoutSession.getId());
        } catch (Exception e) {
            logger.error("Error creating checkout session: {} - {}", checkoutSession.getId(), e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(String sessionId) {
        try {
            RetryUtil.executeWithRetry(
                "deleteById",
                () -> dao.deleteById(sessionId),
                RETRY_CONFIG
            );
        } catch (Exception e) {
            logger.error("Error deleting checkout session: {} - {}", sessionId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<CheckoutSession> updateStatus(String sessionId, CheckoutSession.Status status) {
        try {
            getById(sessionId)
                    .map(x -> {
                        x.getCopyWithUpdatedStatus(status);
                        return x;
                    })
                    .map(CheckoutSessionDynamoDBMapper::toMap)
                    .ifPresent(valueMap -> RetryUtil.executeWithRetry(
                            "updateStatus",
                            () -> dao.save(valueMap),
                            RETRY_CONFIG
                    ));

            return getById(sessionId);
        } catch (Exception e) {
            logger.error("Error updating status for session: {} - {}", sessionId, e.getMessage(), e);
            return Optional.empty();
        }
    }
}
