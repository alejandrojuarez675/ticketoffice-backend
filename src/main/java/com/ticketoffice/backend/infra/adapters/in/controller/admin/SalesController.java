package com.ticketoffice.backend.infra.adapters.in.controller.admin;

import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.infra.adapters.in.controller.CustomController;
import com.ticketoffice.backend.infra.adapters.in.controller.UserRoleValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.SalesListResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import com.ticketoffice.backend.infra.adapters.in.exception.UnauthorizedUserException;
import com.ticketoffice.backend.infra.adapters.in.handlers.SalesHandler;
import com.ticketoffice.backend.infra.adapters.in.utils.IdValidator;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

//@Tag(name = "Sales Management", description = "Endpoints for managing sales")
public class SalesController implements CustomController {

    private final SalesHandler salesHandler;
    private final UserRoleValidator userRoleValidator;

    public SalesController(
            SalesHandler salesHandler,
            UserRoleValidator userRoleValidator
    ) {
        this.salesHandler = salesHandler;
        this.userRoleValidator = userRoleValidator;
    }

    @Override
    public void registeredRoutes(Javalin app) {
        app.get("/api/v1/events/:eventId/sales", context -> {
            String eventId = context.pathParam("eventId");
            context.json(getAllTicketsByEventId(eventId));
        });
        app.post("/api/v1/events/:eventId/sales/:saleId/validate", context -> {
            String saleId = context.pathParam("saleId");
            String eventId = context.pathParam("eventId");
            validateSale(saleId, eventId);
            context.status(HttpStatus.NO_CONTENT);
        });
    }

//    @Operation(
//            summary = "Get all sales for an event",
//            description = "Retrieves all sales for a specific event",
//            security = @SecurityRequirement(name = "Authorization"),
//            parameters = @Parameter(name = "id", description = "The ID of the event", required = true),
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "Successfully retrieved sales",
//                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SalesListResponse.class))
//                    ),
//                    @ApiResponse(
//                            responseCode = "400",
//                            description = "Bad Request",
//                            content = @Content(mediaType = "application/json", examples = {
//                                    @ExampleObject(value = """
//                                    {
//                                      "code": "bad_request",
//                                      "message": "Event id is not valid"
//                                    }
//                                    """)
//                            })
//                    ),
//                    @ApiResponse(
//                            responseCode = "401",
//                            description = "Unauthorized",
//                            content = @Content(mediaType = "application/json", examples = {
//                                    @ExampleObject(value = """
//                                    {
//                                      "code": "unauthorized",
//                                      "message": "You are not authorized to access this resource"
//                                    }
//                                    """)
//                            })
//                    ),
//                    @ApiResponse(
//                            responseCode = "403",
//                            description = "Forbidden",
//                            content = @Content(mediaType = "application/json", examples = {
//                                    @ExampleObject(value = """
//                                    {
//                                      "code": "forbidden",
//                                      "message": "You are not allowed to access this resource"
//                                    }
//                                    """)
//                            })
//                    ),
//                    @ApiResponse(
//                            responseCode = "404",
//                            description = "Event not found",
//                            content = @Content(mediaType = "application/json", examples = {
//                                    @ExampleObject(value = """
//                                    {
//                                      "code": "not_found",
//                                      "message": "Event not found"
//                                    }
//                                    """)
//                            })
//                    )
//            }
//    )
    public SalesListResponse getAllTicketsByEventId(
            String id
    ) throws NotAuthenticatedException, NotFoundException, UnauthorizedUserException, BadRequestException {
        IdValidator.validateIdFromParams(id, "event_id", true);
        userRoleValidator.validateThatUserIsSeller();
        return salesHandler.getAllSalesByEventId(id);
    }

//    @PostMapping("/{saleId}/validate")
//    @Operation(
//            summary = "Validate a sale",
//            description = "Validate a sale for an event",
//            security = @SecurityRequirement(name = "Authorization"),
//            parameters = {
//                    @Parameter(name = "id", description = "The ID of the event", required = true),
//                    @Parameter(name = "saleId", description = "The ID of the sale to be validated", required = true)
//            },
//            responses = {
//                    @ApiResponse(
//                            responseCode = "204",
//                            description = "Successfully validated sale",
//                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SalesListResponse.class))
//                    ),
//                    @ApiResponse(
//                            responseCode = "400",
//                            description = "Bad Request",
//                            content = @Content(mediaType = "application/json", examples = {
//                                    @ExampleObject(value = """
//                                    {
//                                      "code": "bad_request",
//                                      "message": "Event id is not valid"
//                                    }
//                                    """)
//                            })
//                    ),
//                    @ApiResponse(
//                            responseCode = "401",
//                            description = "Unauthorized",
//                            content = @Content(mediaType = "application/json", examples = {
//                                    @ExampleObject(value = """
//                                    {
//                                      "code": "unauthorized",
//                                      "message": "You are not authorized to access this resource"
//                                    }
//                                    """)
//                            })
//                    ),
//                    @ApiResponse(
//                            responseCode = "403",
//                            description = "Forbidden",
//                            content = @Content(mediaType = "application/json", examples = {
//                                    @ExampleObject(value = """
//                                    {
//                                      "code": "forbidden",
//                                      "message": "You are not allowed to access this resource"
//                                    }
//                                    """)
//                            })
//                    ),
//                    @ApiResponse(
//                            responseCode = "404",
//                            description = "Event not found",
//                            content = @Content(mediaType = "application/json", examples = {
//                                    @ExampleObject(value = """
//                                    {
//                                      "code": "not_found",
//                                      "message": "Event not found"
//                                    }
//                                    """)
//                            })
//                    )
//            }
//    )
    public void validateSale(String saleId, String id)
            throws NotAuthenticatedException, NotFoundException, UnauthorizedUserException, BadRequestException {
        IdValidator.validateIdFromParams(id, "event_id", true);
        IdValidator.validateIdFromParams(saleId, "sale_id", true);
        userRoleValidator.validateThatUserIsSeller();
        salesHandler.validateSale(saleId, id);
    }
}
