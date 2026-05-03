package com.github.marcoshssilva.cloud.api.controller.web.impl;

import com.github.marcoshssilva.cloud.api.controller.web.data.WebServerStatus;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.WebServer;
import com.github.marcoshssilva.cloud.api.controller.core.interfaces.Logger;
import com.github.marcoshssilva.cloud.api.controller.web.exceptions.WebServerError;
import com.github.marcoshssilva.cloud.api.controller.core.utils.LoggerHelper;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
@Named("UndertowServerWebServer")
public class UndertowServerWebServerImpl implements WebServer {
    public static final Logger logger = LoggerHelper.getLogger(UndertowServerWebServerImpl.class);

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
            LocalTime now = LocalTime.now();
            server.start();
            LocalTime end = LocalTime.now();

            int nano = Duration.between(now, end).getNano();
            long millis = TimeUnit.NANOSECONDS.toMillis(nano);
            logger.info("Server ready up. Time to startup: {} ms", String.valueOf(millis));

        } catch (Exception e) {
            throw new WebServerError("Failed to start web server", e);
        }
        return this;
    }

    @Override
    public WebServer stop() throws WebServerError {
        try {
            server.stop();
            logger.info("Server stopped with success.");
        } catch (Exception e) {
            throw new WebServerError("Failed to stop web server", e);
        }
        return this;
    }

    @Override
    public WebServerStatus getStatus() {
        try {
            return (server.getListenerInfo().isEmpty() || server.getListenerInfo().stream().allMatch(Undertow.ListenerInfo::isSuspended)) ? WebServerStatus.STOPPED : WebServerStatus.RUNNING;
        } catch (Exception e) {
            logger.error("Failed to get web server status, returning STOPPED as default. Cause: {}", e, e.getMessage());
            return WebServerStatus.STOPPED;
        }

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
