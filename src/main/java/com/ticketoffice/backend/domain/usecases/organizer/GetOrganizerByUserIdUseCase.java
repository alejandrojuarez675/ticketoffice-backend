package com.ticketoffice.backend.domain.usecases.organizer;

import com.ticketoffice.backend.domain.models.Organizer;
import java.util.Optional;

public interface GetOrganizerByUserIdUseCase {

    Optional<Organizer> findByUserId(String userId);
}
