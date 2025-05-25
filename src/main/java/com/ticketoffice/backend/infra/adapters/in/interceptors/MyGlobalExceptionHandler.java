package com.ticketoffice.backend.infra.adapters.in.interceptors;

import com.ticketoffice.backend.infra.adapters.in.dto.response.ErrorResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MyGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage(), "bad_request"));
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<Object> handleNotFoundRequest(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage(), "not_found"));
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage(), "unauthorized"));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        //noinspection CallToPrintStackTrace
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An unexpected error occurred", "internal_server_error"));
    }
}
