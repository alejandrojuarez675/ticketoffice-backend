package com.ticketoffice.backend.integration.tests.auth;

import static com.ticketoffice.backend.integration.mocks.SellerCredentialMocks.SELLER_EMAIL_FOR_SIGNUP;
import static com.ticketoffice.backend.integration.mocks.SellerCredentialMocks.SELLER_PASSWORD_FOR_SIGNUP;
import static com.ticketoffice.backend.integration.mocks.SellerCredentialMocks.SELLER_USERNAME_FOR_SIGNUP;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserSignupRequest;
import com.ticketoffice.backend.integration.calls.AuthCalls;
import com.ticketoffice.backend.integration.config.JavalinIntegrationTest;
import org.junit.jupiter.api.Test;

public class SingUpSellerIntegrationTest {
    @Test
    public void signupAsSeller_shouldReturn200() {
        JavalinIntegrationTest.test((server, client) -> {
            UserSignupRequest bodyRequest = new UserSignupRequest(SELLER_USERNAME_FOR_SIGNUP, SELLER_PASSWORD_FOR_SIGNUP, SELLER_EMAIL_FOR_SIGNUP);
            AuthCalls.signUp(client, bodyRequest);
        });
    }
}
