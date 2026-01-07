package com.ticketoffice.backend.integration;

import static com.ticketoffice.backend.integration.mocks.SellerCredentialMocks.SELLER_EMAIL_FOR_LOGIN;
import static com.ticketoffice.backend.integration.mocks.SellerCredentialMocks.SELLER_PASSWORD_FOR_LOGIN;
import static com.ticketoffice.backend.integration.mocks.SellerCredentialMocks.SELLER_USERNAME_FOR_LOGIN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.google.common.base.Strings;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserLoginRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserSignupRequest;
import com.ticketoffice.backend.integration.calls.AuthCalls;
import com.ticketoffice.backend.integration.config.JavalinIntegrationTest;
import org.junit.jupiter.api.Test;

public class LoginSellerIntegrationTest {
    @Test
    void loginAsSeller_shouldReturn200() {
        JavalinIntegrationTest.test(((server, client) -> {
            UserSignupRequest bodyRequest = new UserSignupRequest(SELLER_USERNAME_FOR_LOGIN, SELLER_PASSWORD_FOR_LOGIN, SELLER_EMAIL_FOR_LOGIN);
            AuthCalls.signUp(client, bodyRequest);

            var loginRequest = new UserLoginRequest(SELLER_USERNAME_FOR_LOGIN, SELLER_PASSWORD_FOR_LOGIN);
            var loginResponse = AuthCalls.login(client, loginRequest);

            assertFalse(Strings.isNullOrEmpty(loginResponse.token()));
            assertTrue(loginResponse.expiresIn() > 0L);
        }));
    }
}
