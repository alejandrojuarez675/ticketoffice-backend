package com.ticketoffice.backend.integration.tests.configs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.response.config.CountryDto;
import com.ticketoffice.backend.integration.config.JavalinIntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;

public class GetCountriesIntegrationTest {
    @Test
    void getCountryList_shouldReturn200(){
        JavalinIntegrationTest.test((server, client) -> {
            var response = client.get("/api/public/v1/form/country");
            assertEquals(200, response.code());
            assertNotNull(response.body());

            ObjectMapper objectMapper = new ObjectMapper();

            var body = response.body().string();
            var json = objectMapper.readValue(body, new TypeReference<List<CountryDto>>() {});

            assertEquals(2, json.size());

            var codeList = json.stream().map(CountryDto::getCode).toList();
            assertTrue(codeList.contains("ARG"));
            assertTrue(codeList.contains("COL"));
        });
    }
}
