package com.ticketoffice.backend.domain.ports;

import com.ticketoffice.backend.domain.models.Sale;
import java.util.List;
import java.util.Optional;

public interface SaleRepository {
    Optional<Sale> save(Sale sale);

    /**
     * Counts the number of sales for a specific event and ticket.
     *
     * @param eventId The ID of the event
     * @param ticketId The ID of the ticket (optional, can be null to count all sales for the event)
     * @return The count of matching sales
     */
    Integer countByEventIdAndTicketId(String eventId, String ticketId);

    /**
     * Finds all sales for a specific event.
     *
     * @param eventId The ID of the event
     * @return A list of sales for the specified event
     */
    List<Sale> findByEventId(String eventId);

    /**
     * Retrieves a sale by its ID.
     *
     * @param id The ID of the sale
     * @return An Optional containing the sale if found, or empty if not found
     */
    Optional<Sale> getById(String id);

    /**
     * Updates an existing sale.
     *
     * @param id The ID of the sale to update
     * @param sale The updated sale data
     * @return An Optional containing the updated sale if successful, or empty if the sale was not found
     */
    Optional<Sale> update(String id, Sale sale);
}
