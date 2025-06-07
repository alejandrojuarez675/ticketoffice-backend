package com.ticketoffice.backend.infra.adapters.out.cache;

import com.ticketoffice.backend.application.utils.CheckoutSessionIdUtils;
import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.ports.CheckoutSessionCache;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CheckoutSessionInMemoryCache implements CheckoutSessionCache {

    private static final Map<String, CheckoutSession> data = new HashMap<>();

    /**
     * Remove expired sessions.
     * This method should be called periodically to remove expired sessions or before each request.
     **/
    private void removeExpiredSessions() {
        data.values().stream()
                .filter(CheckoutSession::isExpired)
                .forEach(session -> data.remove(session.getId()));
    }

    @Override
    public Integer countKeysMatches(String pattern) {
        removeExpiredSessions();
        return data.keySet().stream()
                .filter(key -> key.matches(pattern))
                .map(CheckoutSessionIdUtils::getQuantity)
                .reduce(0, Integer::sum);
    }

    @Override
    public Optional<CheckoutSession> getById(String sessionId) {
        System.out.println("[Before expering] Getting checkout session by id: " + sessionId + " on " + data);
        removeExpiredSessions();
        System.out.println("[After expering] Getting checkout session by id: " + sessionId + " on " + data);
        return Optional.ofNullable(data.get(sessionId));
    }

    @Override
    public Optional<CheckoutSession> createCheckoutSession(CheckoutSession checkoutSession) {
        removeExpiredSessions();
        data.put(checkoutSession.getId(), checkoutSession);
        return getById(checkoutSession.getId());
    }

    @Override
    public void deleteById(String sessionId) {
        data.remove(sessionId);
    }

    @Override
    public Optional<CheckoutSession> updateStatus(String sessionId, CheckoutSession.Status status) {
        return getById(sessionId)
                .map(session -> session.getCopyWithUpdatedStatus(status))
                .map(session -> data.replace(sessionId, session));
    }
}
