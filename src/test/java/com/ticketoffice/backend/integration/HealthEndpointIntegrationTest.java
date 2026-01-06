package com.ticketoffice.backend.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketoffice.backend.integration.config.MyJavalinTest;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class HealthEndpointIntegrationTest {
    
    @Test
    void getHealth_shouldReturn200(){
        MyJavalinTest.test((server, client) -> {
            var response = client.get("/health");
            assertEquals(200, response.code());
            assertNotNull(response.body());

            var body = response.body().string();
            var json = new ObjectMapper().readValue(body, Map.class);
            assertEquals("healthy", json.get("status"));
        });
    }
}
