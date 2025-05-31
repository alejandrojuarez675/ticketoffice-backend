package com.ticketoffice.backend.infra.adapters.out.cache;

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
        return Math.toIntExact(data.keySet().stream().filter(key -> key.matches(pattern)).count());
    }

    @Override
    public Optional<CheckoutSession> getById(String sessionId) {
        removeExpiredSessions();
        return Optional.ofNullable(data.get(sessionId));
    }

    @Override
    public Optional<CheckoutSession> createCheckoutSession(CheckoutSession checkoutSession) {
        removeExpiredSessions();
        data.put(checkoutSession.getId(), checkoutSession);
        return getById(checkoutSession.getId());
    }
}
