package com.ticketoffice.backend.domain.ports;

import com.ticketoffice.backend.domain.models.Purchase;
import java.util.Optional;

public interface PurchaseRepository {
    Optional<Purchase> save(Purchase purchase);
}
