package com.github.marcoshssilva.cloud.api.controller.web;

import com.github.marcoshssilva.cloud.api.controller.core.exceptions.ApplicationStartupErrorException;
import com.github.marcoshssilva.cloud.api.controller.web.exceptions.WebServerError;
import com.github.marcoshssilva.cloud.api.controller.core.interfaces.ApplicationRunner;
import com.github.marcoshssilva.cloud.api.controller.core.interfaces.Logger;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.WebServer;
import com.github.marcoshssilva.cloud.api.controller.core.utils.LoggerHelper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.LocalTime;


@Named("AppStartup")
@ApplicationScoped
public class WebStartupInitializer implements ApplicationRunner {
    public static final Logger logger = LoggerHelper.getLogger(WebStartupInitializer.class);
    public static final Thread CURRENT_THREAD = Thread.currentThread();

    private final WebServer webServer;

    public @Inject WebStartupInitializer(@Named("UndertowServerWebServer") WebServer webServer) {
        this.webServer = webServer;
    }

    @Override
    public void run(String... args) throws ApplicationStartupErrorException {
        LocalTime now = LocalTime.now();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                logger.info("Shutting down web server...");
                webServer.stop();
            } catch (WebServerError e) {
                logger.error("Failed to stop web server during shutdown. Cause: {}", e, e.getMessage());
            }
        }));

        try (WebServer server = webServer.start()) {
            logger.info("Web server started at {} on port {}", server.getHost(), String.valueOf(server.getPort()));
            logger.info("Management server started at {} on port {}", server.getManagementHost(), String.valueOf(server.getManagementPort()));
            CURRENT_THREAD.join();
        } catch (Exception e) {
            throw new ApplicationStartupErrorException("Failed to start web server", e);
        }
    }
}
