package com.ticketoffice.backend.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.response.config.CountryConfigDto;
import com.ticketoffice.backend.integration.config.JavalinIntegrationTest;
import org.junit.jupiter.api.Test;

public class GetArgentinaConfigIntegrationTest {
    @Test
    void getArgentinaConfig_shouldReturn200(){
        JavalinIntegrationTest.test((server, client) -> {
            var response = client.get("/api/public/v1/form/country/ARG/config");
            assertEquals(200, response.code());
            assertNotNull(response.body());

            var body = response.body().string();
            var json = new ObjectMapper().readValue(body, CountryConfigDto.class);

            assertEquals("ARG", json.data().getCode());
            assertEquals("Argentina", json.data().getName());
        });
    }
}
