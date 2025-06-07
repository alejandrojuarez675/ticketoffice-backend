package com.ticketoffice.backend.domain.ports;

import com.ticketoffice.backend.domain.models.Sale;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface SaleRepository {
    Optional<Sale> save(Sale sale);

    Integer count(Predicate<Sale> predicate);

    List<Sale> findByEventId(String eventId);
}
