package com.github.marcoshssilva.cloud.api.controller.web.interfaces;

import com.github.marcoshssilva.cloud.api.controller.web.data.HttpMethod;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

public interface HttpRequest {
    HttpMethod getMethod();
    String getPath();
    Collection<HttpHeaders> getHeaders();
    Collection<HttpCookie> getCookies();
    byte[] getBody();

    default String getBodyAsString() {
        return new String(getBody(), StandardCharsets.UTF_8);
    }
}
