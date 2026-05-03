package com.github.marcoshssilva.cloud.api.controller.web.data;

public record HttpCookie(String name, String value, String domain, String path, long maxAge, boolean isSecure, boolean isHttpOnly) { }
