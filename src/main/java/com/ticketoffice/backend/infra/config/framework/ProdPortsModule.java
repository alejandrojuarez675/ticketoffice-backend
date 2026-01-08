package com.ticketoffice.backend.infra.config.framework;

import com.google.inject.AbstractModule;
import com.ticketoffice.backend.domain.ports.CheckoutSessionCache;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.ports.MailSenderPort;
import com.ticketoffice.backend.domain.ports.UserTokenRepository;
import com.ticketoffice.backend.domain.ports.RegionalizationRepository;
import com.ticketoffice.backend.domain.ports.SaleRepository;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.infra.adapters.out.cache.dynamo.CheckoutSessionDynamoRepository;
import com.ticketoffice.backend.infra.adapters.out.db.repository.event.EventDynamoRepository;
import com.ticketoffice.backend.infra.adapters.out.db.repository.passwordresettoken.UserTokenDynamoRepository;
import com.ticketoffice.backend.infra.adapters.out.db.repository.sale.SaleDynamoRepository;
import com.ticketoffice.backend.infra.adapters.out.db.repository.user.UserDynamoRepository;
import com.ticketoffice.backend.infra.adapters.out.emails.SesMailSenderAdapter;
import com.ticketoffice.backend.infra.config.RegionalizationInMemoryRepository;
import com.ticketoffice.backend.infra.config.SesModule;

public class ProdPortsModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new SesModule());
        bind(MailSenderPort.class).to(SesMailSenderAdapter.class);

        bind(UserRepository.class).to(UserDynamoRepository.class);
        bind(EventRepository.class).to(EventDynamoRepository.class);
        bind(SaleRepository.class).to(SaleDynamoRepository.class);
        bind(CheckoutSessionCache.class).to(CheckoutSessionDynamoRepository.class);
        bind(UserTokenRepository.class).to(UserTokenDynamoRepository.class);
        bind(RegionalizationRepository.class).to(RegionalizationInMemoryRepository.class);
    }
}
