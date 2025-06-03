package com.ticketoffice.backend.application.usecases.checkout;

import com.ticketoffice.backend.domain.models.Purchase;
import com.ticketoffice.backend.domain.ports.PurchaseRepository;
import com.ticketoffice.backend.domain.usecases.checkout.SavePurchaseUseCase;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class SavePurchaseUseCaseImpl implements SavePurchaseUseCase {

    private final PurchaseRepository purchaseRepository;

    public SavePurchaseUseCaseImpl(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public Optional<Purchase> apply(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }
}
