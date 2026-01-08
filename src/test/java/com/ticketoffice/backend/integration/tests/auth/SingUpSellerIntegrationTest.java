package com.ticketoffice.backend.integration.tests.auth;

import static com.ticketoffice.backend.integration.mocks.SellerCredentialMocks.SELLER_EMAIL_FOR_SIGNUP;
import static com.ticketoffice.backend.integration.mocks.SellerCredentialMocks.SELLER_PASSWORD_FOR_SIGNUP;
import static com.ticketoffice.backend.integration.mocks.SellerCredentialMocks.SELLER_USERNAME_FOR_SIGNUP;
import com.ticketoffice.backend.integration.calls.AuthCalls;
import com.ticketoffice.backend.integration.config.JavalinIntegrationTest;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class SingUpSellerIntegrationTest {
    @Test
    public void signupAsSeller_shouldReturn200() {
        JavalinIntegrationTest.test((server, client) -> {
            var bodyRequest = Map.of(
                    "username", SELLER_USERNAME_FOR_SIGNUP,
                    "password", SELLER_PASSWORD_FOR_SIGNUP,
                    "email", SELLER_EMAIL_FOR_SIGNUP
            );
            AuthCalls.signUp(client, bodyRequest);
        });
    }
}
