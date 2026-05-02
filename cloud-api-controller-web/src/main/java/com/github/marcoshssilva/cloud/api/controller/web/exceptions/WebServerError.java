package com.github.marcoshssilva.cloud.api.controller.web.exceptions;

public class WebServerError extends Exception {
    public WebServerError(String message, Exception cause) {
        super(message, cause);
    }
}
