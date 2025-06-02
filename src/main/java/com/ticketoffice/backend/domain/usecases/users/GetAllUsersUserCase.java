package com.ticketoffice.backend.domain.usecases.users;

import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.List;
import java.util.function.Supplier;

@FunctionalInterface
public interface GetAllUsersUserCase extends UseCase, Supplier<List<User>> {
}
