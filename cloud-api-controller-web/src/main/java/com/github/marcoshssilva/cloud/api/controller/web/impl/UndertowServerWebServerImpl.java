package com.github.marcoshssilva.cloud.api.controller.web.impl;

import com.github.marcoshssilva.cloud.api.controller.web.WebServer;
import com.github.marcoshssilva.cloud.api.controller.web.exceptions.WebServerError;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("UndertowServerWebServer")
public class UndertowServerWebServerImpl implements WebServer {
    private final Undertow server;
    private final int port;
    private final int managementPort;
    private final String host;
    private final String managementHost;

    public UndertowServerWebServerImpl() {
        this.managementPort = 8081;
        this.managementHost = "0.0.0.0";
        this.port = 8080;
        this.host = "0.0.0.0";

        this.server = Undertow.builder().addHttpListener(port, host).addHttpListener(managementPort, managementHost)
                .setHandler(this.buildHttpHandler())
                .build();
    }

    @Override
    public WebServer start() throws WebServerError {
        try {
            server.start();
        } catch (Exception e) {
            throw new WebServerError("Failed to start web server", e);
        }
        return this;
    }

    @Override
    public WebServer stop() throws WebServerError {
        try {
            server.stop();
        } catch (Exception e) {
            throw new WebServerError("Failed to stop web server", e);
        }
        return this;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public int getManagementPort() {
        return this.managementPort;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public String getManagementHost() {
        return this.managementHost;
    }

    @Override
    public void close() {
        server.stop();
    }

    HttpHandler buildHttpHandler() {
        return (exchange) -> exchange.getResponseSender().send("Hello World!");
    }
}
