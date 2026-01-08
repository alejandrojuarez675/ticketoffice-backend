package com.ticketoffice.backend.domain.usecases.emails;

import com.ticketoffice.backend.domain.models.User;
import java.util.function.Consumer;

@FunctionalInterface
public interface SendConfirmAccountEmail extends Consumer<User> {
}
