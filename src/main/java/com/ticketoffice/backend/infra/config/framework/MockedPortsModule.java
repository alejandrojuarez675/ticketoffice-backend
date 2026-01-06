package com.ticketoffice.backend.infra.config.framework;

import com.google.inject.AbstractModule;
import com.ticketoffice.backend.domain.ports.CheckoutSessionCache;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.ports.MailSenderPort;
import com.ticketoffice.backend.domain.ports.PasswordResetTokenRepository;
import com.ticketoffice.backend.domain.ports.RegionalizationRepository;
import com.ticketoffice.backend.domain.ports.SaleRepository;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.infra.adapters.out.cache.CheckoutSessionInMemoryCache;
import com.ticketoffice.backend.infra.adapters.out.db.repository.event.EventInMemoryRepository;
import com.ticketoffice.backend.infra.adapters.out.db.repository.password.PasswordResetTokenInMemoryRepository;
import com.ticketoffice.backend.infra.adapters.out.db.repository.sale.SaleInMemoryRepository;
import com.ticketoffice.backend.infra.adapters.out.db.repository.user.UserInMemoryRepository;
import com.ticketoffice.backend.infra.adapters.out.emails.LogMailSenderAdapter;
import com.ticketoffice.backend.infra.config.RegionalizationInMemoryRepository;

public class MockedPortsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MailSenderPort.class).to(LogMailSenderAdapter.class);
        bind(EventRepository.class).to(EventInMemoryRepository.class);
        bind(UserRepository.class).to(UserInMemoryRepository.class);
        bind(SaleRepository.class).to(SaleInMemoryRepository.class);
        bind(CheckoutSessionCache.class).to(CheckoutSessionInMemoryCache.class);
        bind(PasswordResetTokenRepository.class).to(PasswordResetTokenInMemoryRepository.class);
        bind(RegionalizationRepository.class).to(RegionalizationInMemoryRepository.class);
    }
}
