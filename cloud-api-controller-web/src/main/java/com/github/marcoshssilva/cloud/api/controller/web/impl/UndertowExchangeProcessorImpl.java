package com.github.marcoshssilva.cloud.api.controller.web.impl;

import com.github.marcoshssilva.cloud.api.controller.web.data.HttpCookie;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpHeader;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpMethod;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpRequest;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpRequestProcessor;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpResponse;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class UndertowExchangeProcessorImpl {
    public void process(HttpServerExchange exchange, HttpRequestProcessor processor) {
        exchange.dispatch(() -> {
            exchange.startBlocking();
            byte[] bytes;
            try (java.io.InputStream is = exchange.getInputStream()) {
                bytes = is.readAllBytes();
            } catch (java.io.IOException e) {
                exchange.setStatusCode(500);
                exchange.getResponseSender().send("Internal Server Error");
                return;
            }
            HttpRequest  request  = this.buildHttpRequest(exchange, bytes);
            HttpResponse response = processor.process(request);

            exchange.setStatusCode(response.getStatusCode().getCode());
            response.getHeaders().forEach(header -> {
                for(String value : header.value()) {
                    exchange.getResponseHeaders().add(new HttpString(header.name()), value);
                }
            });
            exchange.getResponseSender().send(ByteBuffer.wrap(response.getBody()));
        });
    }

    HttpRequest buildHttpRequest(HttpServerExchange exchange, byte[] body) {
        return new HttpRequestImpl(
                HttpMethod.findByName(exchange.getRequestMethod().toString()),
                exchange.getRelativePath(),
                this.buildHeaders(exchange),
                this.buildCookies(exchange),
                body
        );
    }

    Collection<HttpHeader> buildHeaders(HttpServerExchange exchange) {
        List<HttpHeader> headers = new ArrayList<>();
        for (HeaderValues headerValues : exchange.getRequestHeaders()) {
            String name = headerValues.getHeaderName().toString();
            headers.add(new HttpHeader(name, new HashSet<>(headerValues)));
        }
        return headers;
    }

    Collection<HttpCookie> buildCookies(HttpServerExchange exchange) {
        List<HttpCookie> cookies = new ArrayList<>();
        for (io.undertow.server.handlers.Cookie cookie : exchange.requestCookies()) {
            long maxAgeLong = cookie.getMaxAge() != null ? cookie.getMaxAge().longValue() : -1L;
            cookies.add(new HttpCookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), maxAgeLong, cookie.isSecure(), cookie.isHttpOnly()));
        }
        return cookies;
    }
}
