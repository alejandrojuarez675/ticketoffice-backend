package com.ticketoffice.backend.integration.tests.auth;

import static com.ticketoffice.backend.integration.mocks.SellerCredentialMocks.SELLER_EMAIL_FOR_LOGIN;
import static com.ticketoffice.backend.integration.mocks.SellerCredentialMocks.SELLER_PASSWORD_FOR_LOGIN;
import static com.ticketoffice.backend.integration.mocks.SellerCredentialMocks.SELLER_USERNAME_FOR_LOGIN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.google.common.base.Strings;
import com.ticketoffice.backend.integration.calls.AuthCalls;
import com.ticketoffice.backend.integration.config.JavalinIntegrationTest;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class LoginSellerIntegrationTest {
    @Test
    void loginAsSeller_shouldReturn200() {
        JavalinIntegrationTest.test(((server, client) -> {
            Map<String, String> bodyRequest = Map.of(
                    "username", SELLER_USERNAME_FOR_LOGIN,
                    "password", SELLER_PASSWORD_FOR_LOGIN,
                    "email", SELLER_EMAIL_FOR_LOGIN
            );
            AuthCalls.signUp(client, bodyRequest);

            var loginRequest = Map.of(
                    "username", SELLER_USERNAME_FOR_LOGIN,
                    "password", SELLER_PASSWORD_FOR_LOGIN
            );
            var loginResponse = AuthCalls.login(client, loginRequest);

            assertFalse(Strings.isNullOrEmpty(loginResponse.token()));
            assertTrue(loginResponse.expiresIn() > 0L);
        }));
    }
}
