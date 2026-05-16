package com.github.marcoshssilva.cloud.api.controller.web.impl;

import com.github.marcoshssilva.cloud.api.controller.web.data.HttpCookie;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpHeader;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpStatusCode;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HttpResponseImpl implements HttpResponse {
    private Collection<HttpHeader> headers = new ArrayList<>(0);
    private Collection<HttpCookie> cookies = new ArrayList<>(0);
    private byte[] body;
    private HttpStatusCode statusCode;

    public HttpResponseImpl() { }

    public HttpResponseImpl(Collection<HttpHeader> headers, Collection<HttpCookie> cookies, byte[] body, HttpStatusCode statusCode) {
        this.headers = headers;
        this.cookies = cookies;
        this.body = body;
        this.statusCode = statusCode;
    }

    @Override
    public Collection<HttpHeader> getHeaders() {
        return this.headers;
    }

    @Override
    public Collection<HttpCookie> getCookies() {
        return this.cookies;
    }

    @Override
    public byte[] getBody() {
        return this.body;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return this.statusCode;
    }

    public void setHeaders(Collection<HttpHeader> headers) {
        this.headers = headers;
    }

    public void setCookies(Collection<HttpCookie> cookies) {
        this.cookies = cookies;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
