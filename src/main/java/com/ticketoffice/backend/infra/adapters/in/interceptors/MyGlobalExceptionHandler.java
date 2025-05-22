package com.ticketoffice.backend.infra.adapters.in.interceptors;

import com.ticketoffice.backend.infra.adapters.in.dto.response.ErrorResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
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
}
