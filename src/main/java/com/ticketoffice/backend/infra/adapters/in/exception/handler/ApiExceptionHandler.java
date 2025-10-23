package com.ticketoffice.backend.infra.adapters.in.exception.handler;

import com.ticketoffice.backend.infra.adapters.in.dto.response.ErrorResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import com.ticketoffice.backend.infra.adapters.in.exception.UnauthorizedUserException;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.jetbrains.annotations.NotNull;

public class ApiExceptionHandler implements ExceptionHandler<Exception> {

    @Override
    public void handle(@NotNull Exception exception, @NotNull Context ctx) {
        exception.printStackTrace();
        if (exception instanceof BadRequestException) {
            handleBadRequest((BadRequestException) exception, ctx);
        } else if (exception instanceof NotFoundException) {
            handleNotFound((NotFoundException) exception, ctx);
        } else if (exception instanceof UnauthorizedUserException) {
            handleUnauthorized((UnauthorizedUserException) exception, ctx);
        } else {
            // Default to 500 for unhandled exceptions
            handleGenericException(exception, ctx);
        }
    }

    private void handleBadRequest(BadRequestException exception, Context ctx) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                "BAD_REQUEST"
        );
        ctx.status(400).json(errorResponse);
    }

    private void handleNotFound(NotFoundException exception, Context ctx) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                "NOT_FOUND"
        );
        ctx.status(404).json(errorResponse);
    }

    private void handleUnauthorized(UnauthorizedUserException exception, Context ctx) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                "UNAUTHORIZED"
        );
        ctx.status(401).json(errorResponse);
    }

    private void handleGenericException(Exception exception, Context ctx) {
        ErrorResponse errorResponse = new ErrorResponse(
                "An unexpected error occurred: " + exception.getMessage(),
                "INTERNAL_SERVER_ERROR"
        );
        ctx.status(500).json(errorResponse);
    }
}
