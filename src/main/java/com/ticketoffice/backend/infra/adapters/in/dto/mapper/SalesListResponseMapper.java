package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.SalesListResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SalesListResponseMapper {

    public static SalesListResponse toResponse(List<Sale> sales, Event event) {
        return new SalesListResponse(
                sales.stream()
                        .map(sale -> SaleLightDTOMapper.getFromSale(sale, event))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
    }
}
