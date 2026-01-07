package com.ticketoffice.backend.integration.tests.health;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.ticketoffice.backend.integration.config.JavalinIntegrationTest;
import org.junit.jupiter.api.Test;

class PingEndpointIntegrationTest {
    @Test
    void getPing_shouldReturn200(){
        JavalinIntegrationTest.test((server, client) -> {
            var response = client.get("/ping");
            assertEquals(200, response.code());
            assertNotNull(response.body());
            assertEquals("pong", response.body().string());
        });
    }
}
