package com.ticketoffice.backend.integration;

import static com.ticketoffice.backend.integration.mocks.SellerMocks.SELLER_EMAIL_FOR_SIGNUP;
import static com.ticketoffice.backend.integration.mocks.SellerMocks.SELLER_PASSWORD_FOR_SIGNUP;
import static com.ticketoffice.backend.integration.mocks.SellerMocks.SELLER_USERNAME_FOR_SIGNUP;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.google.common.base.Strings;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserSignupRequest;
import com.ticketoffice.backend.integration.calls.AuthCalls;
import com.ticketoffice.backend.integration.config.JavalinIntegrationTest;
import org.junit.jupiter.api.Test;

public class SingUpSellerIntegrationTest {
    @Test
    public void signupAsSeller_shouldReturn200() {
        JavalinIntegrationTest.test((server, client) -> {
            UserSignupRequest bodyRequest = new UserSignupRequest(SELLER_USERNAME_FOR_SIGNUP, SELLER_PASSWORD_FOR_SIGNUP, SELLER_EMAIL_FOR_SIGNUP);
            var json = AuthCalls.signUp(client, bodyRequest);

            assertFalse(Strings.isNullOrEmpty(json.token()));
            assertTrue(json.expiresIn() > 0L);
        });
    }
}
