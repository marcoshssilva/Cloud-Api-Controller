package com.github.marcoshssilva.cloud.api.controller.core.exceptions;

public class ApplicationStartupErrorException extends Exception {
    public ApplicationStartupErrorException(String message, Exception cause) {
        super(message, cause);
    }
}
