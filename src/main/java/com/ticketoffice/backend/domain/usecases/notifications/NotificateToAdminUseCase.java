package com.ticketoffice.backend.domain.usecases.notifications;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface NotificateToAdminUseCase extends BiConsumer<String, String> {
}
