package com.github.marcoshssilva.cloud.api.controller.core.exceptions;

public class WebServerError extends Exception {
    public WebServerError(String message, Exception cause) {
        super(message, cause);
    }
}
