package com.ticketoffice.backend.infra.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.SesAsyncClientBuilder;

import java.net.URI;

public class SesModule extends AbstractModule {
    private static final Logger log = LoggerFactory.getLogger(SesModule.class);

    @Override
    protected void configure() {
        // Bindings are provided by @Provides methods
    }

    @Provides
    @Singleton
    public SesAsyncClient provideSesClient() {
        String region = System.getenv("AWS_REGION");
        if (region == null || region.isBlank()) {
            throw new IllegalStateException("AWS_REGION environment variable is required");
        }

        SesAsyncClientBuilder builder = SesAsyncClient.builder()
            .region(Region.of(region));

        String sesEndpoint = System.getenv("AWS_SES_ENDPOINT_OVERRIDE");
        if (sesEndpoint != null && !sesEndpoint.isBlank()) {
            log.info("Using SES endpoint override: {}", sesEndpoint);
            builder.endpointOverride(URI.create(sesEndpoint));
        }

        return builder.build();
    }

    @Provides
    @Singleton
    public String provideSenderEmail() {
        String senderEmail = System.getenv("AWS_SES_SENDER");
        if (senderEmail == null || senderEmail.isBlank()) {
            throw new IllegalStateException("AWS_SES_SENDER environment variable is required");
        }
        return senderEmail;
    }
}
