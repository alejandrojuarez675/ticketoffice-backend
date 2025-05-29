package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.utils.EventSearchParameters;

public interface CountEventsByParamsUseCase {
    Integer countEventsByParams(EventSearchParameters eventSearchParameters);
}
