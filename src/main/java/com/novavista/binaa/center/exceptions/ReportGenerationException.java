package com.novavista.binaa.center.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ReportGenerationException extends RuntimeException {
    public ReportGenerationException(String message) {
        super(message);
    }
}