package com.github.marcoshssilva.cloud.api.controller.web.interfaces;

import com.github.marcoshssilva.cloud.api.controller.web.data.HttpCookie;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpHeader;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpStatusCode;

import java.util.Collection;

public interface HttpResponse {
    Collection<HttpHeader> getHeaders();
    Collection<HttpCookie> getCookies();
    byte[] getBody();
    HttpStatusCode getStatusCode();
}
