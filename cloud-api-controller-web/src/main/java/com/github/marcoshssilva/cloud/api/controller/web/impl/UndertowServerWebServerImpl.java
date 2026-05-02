package com.github.marcoshssilva.cloud.api.controller.web.impl;

import com.github.marcoshssilva.cloud.api.controller.web.WebServer;
import com.github.marcoshssilva.cloud.api.controller.web.exceptions.WebServerError;
import com.github.marcoshssilva.cloud.api.controller.web.utils.LoggerHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@ApplicationScoped
@Named("UndertowServerWebServer")
public class UndertowServerWebServerImpl implements WebServer {
    private static final Logger logger = LoggerHelper.getLogger(UndertowServerWebServerImpl.class);

    private final int port;
    public UndertowServerWebServerImpl() {
        this.port = 8080;
    }

    @Override
    public WebServer start() throws WebServerError {
        logger.info("Starting web server on port {}", port);
        return this;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    public void stop() throws WebServerError {
        logger.info("Stopping web server on port {}", port);
    }

    @Override
    public void close() throws IOException {
        try {
            this.stop();
        } catch (WebServerError e) {
            throw new IOException(e);
        }
    }
}
