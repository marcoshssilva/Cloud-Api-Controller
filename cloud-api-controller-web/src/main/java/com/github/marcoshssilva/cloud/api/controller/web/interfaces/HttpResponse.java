package com.github.marcoshssilva.cloud.api.controller.web.interfaces;

import java.util.Collection;

public interface HttpResponse {
    Collection<HttpHeaders> getHeaders();
    Collection<HttpCookie> getCookies();
    byte[] getBody();
}
