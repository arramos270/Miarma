package com.example.Miarma.Validation;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class GlobalRestControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildApiError(ex, request, ex.getFieldErrors()
                .stream().map(error -> ApiValidationSubError.builder()
                        .objeto(error.getObjectName())
                        .campo(error.getField())
                        .valorRechazado(error.getRejectedValue())
                        .mensaje(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList())
        );
    }



    private ResponseEntity<Object> buildApiError(Exception ex, WebRequest request, List<ApiSubError> subErrores) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), ((ServletWebRequest) request).getRequest().getRequestURI(), subErrores));

    }

    // Resto del código

}

