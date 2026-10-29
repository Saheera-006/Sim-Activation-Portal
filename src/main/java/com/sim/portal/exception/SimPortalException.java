package com.sim.portal.exception;

import org.springframework.http.HttpStatus;

public class SimPortalException extends RuntimeException {
    private final HttpStatus status;

    public SimPortalException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() { return status; }
}
