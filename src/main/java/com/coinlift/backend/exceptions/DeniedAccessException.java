package com.coinlift.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DeniedAccessException extends RuntimeException {
    public DeniedAccessException(String message) {
        super(message);
    }
}
