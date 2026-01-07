package com.ticketoffice.backend.integration.calls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserLoginRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserSignupRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.LoginResponse;
import io.javalin.testtools.HttpClient;
import java.io.IOException;

public class AuthCalls {

    public static LoginResponse signUp(HttpClient client, UserSignupRequest bodyRequest) throws IOException {
        var response = client.post("/auth/signup", bodyRequest);
        assertEquals(200, response.code());
        assertNotNull(response.body());

        var body = response.body().string();
        return new ObjectMapper().readValue(body, LoginResponse.class);
    }

    public static LoginResponse login(HttpClient client, UserLoginRequest bodyRequest) throws IOException {
        var response = client.post("/auth/login", bodyRequest);
        assertEquals(200, response.code());
        assertNotNull(response.body());

        var body = response.body().string();
        return new ObjectMapper().readValue(body, LoginResponse.class);
    }
}
