package com.ticketoffice.backend.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserSignupRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.LoginResponse;
import com.ticketoffice.backend.integration.config.JavalinIntegrationTest;
import org.junit.jupiter.api.Test;

public class SingUpSellerIntegrationTest {
    @Test
    public void signupAsSeller_shouldReturn200() {
        JavalinIntegrationTest.test((server, client) -> {
            UserSignupRequest bodyRequest = new UserSignupRequest("seller", "seller1234", "seller@gmail.com");
            var response = client.post("/auth/signup", bodyRequest);
            assertEquals(200, response.code());
            assertNotNull(response.body());

            var body = response.body().string();
            var json = new ObjectMapper().readValue(body, LoginResponse.class);

            assertFalse(Strings.isNullOrEmpty(json.token()));
            assertTrue(json.expiresIn() > 0L);
        });
    }
}
