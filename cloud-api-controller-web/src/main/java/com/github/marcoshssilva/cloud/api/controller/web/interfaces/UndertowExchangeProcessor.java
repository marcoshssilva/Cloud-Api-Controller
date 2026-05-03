package com.github.marcoshssilva.cloud.api.controller.web.interfaces;

import io.undertow.server.HttpServerExchange;

public interface UndertowExchangeProcessor {
    void process(HttpServerExchange exchange, HttpRequestProcessor processor);
}
