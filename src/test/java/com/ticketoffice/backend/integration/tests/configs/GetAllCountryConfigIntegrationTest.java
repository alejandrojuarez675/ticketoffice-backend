package com.ticketoffice.backend.integration.tests.configs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.response.config.CountryConfigDto;
import com.ticketoffice.backend.infra.adapters.in.dto.response.config.CountryDto;
import com.ticketoffice.backend.integration.config.JavalinIntegrationTest;
import java.io.IOException;
import java.util.List;
import okhttp3.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GetAllCountryConfigIntegrationTest {

    @Test
    void getAllCountryConfig_shouldReturn200() {
        JavalinIntegrationTest.test((server, client) -> {
            var responseAll = client.get("/api/public/v1/form/country");
            assertEquals(200, responseAll.code());
            assertNotNull(responseAll.body());

            ObjectMapper objectMapper = new ObjectMapper();

            var bodyAll = responseAll.body().string();
            var jsonAll = objectMapper.readValue(bodyAll, new TypeReference<List<CountryDto>>() {
            });

            jsonAll.forEach(country -> {
                Response response = client.get(String.format("/api/public/v1/form/country/%s/config", country.getCode()));

                Assertions.assertEquals(200, response.code());
                assertNotNull(response.body());
                try {
                    var body = response.body().string();
                    CountryConfigDto json = new ObjectMapper().readValue(body, CountryConfigDto.class);
                    assertEquals(country.getCode(), json.data().getCode());
                    assertEquals(country.getName(), json.data().getName());
                } catch (IOException e) {
                    Assertions.fail(e);
                }
            });
        });
    }
}
