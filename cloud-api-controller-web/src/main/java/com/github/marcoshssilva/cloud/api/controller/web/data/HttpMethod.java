package com.github.marcoshssilva.cloud.api.controller.web.data;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    HEAD,
    OPTIONS
    ;

    public static HttpMethod findByName(String name) {
        return HttpMethod.valueOf(name.toUpperCase());
    }
}
