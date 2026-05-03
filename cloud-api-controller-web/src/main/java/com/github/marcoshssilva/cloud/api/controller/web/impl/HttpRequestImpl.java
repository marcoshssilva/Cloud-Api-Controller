package com.github.marcoshssilva.cloud.api.controller.web.impl;

import com.github.marcoshssilva.cloud.api.controller.web.data.HttpCookie;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpHeader;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpMethod;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpRequest;

import java.util.Collection;
import java.util.List;

public class HttpRequestImpl implements HttpRequest {
    private HttpMethod method;
    private String path;
    private Collection<HttpHeader> headers = List.of();
    private Collection<HttpCookie> cookies = List.of();
    private byte[] body;

    public HttpRequestImpl() { }

    public HttpRequestImpl(HttpMethod method, String path, Collection<HttpHeader> headers, Collection<HttpCookie> cookies, byte[] body) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.cookies = cookies;
        this.body = body;
    }

    @Override
    public HttpMethod getMethod() {
        return this.method;
    }

    @Override
    public String getPath() {
        return this.path;
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
}
