package com.github.marcoshssilva.cloud.api.controller.web;

import com.github.marcoshssilva.cloud.api.controller.web.exceptions.ApplicationStartupErrorException;
import com.github.marcoshssilva.cloud.api.controller.web.utils.LoggerHelper;
import org.apache.logging.log4j.Logger;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named("AppStartup")
@ApplicationScoped
public class App implements ApplicationRunner {
    public static final Logger logger = LoggerHelper.getLogger(App.class);

    private final WebServer webServer;

    public @Inject App(WebServer webServer) {
        this.webServer = webServer;
    }

    public static void main(String[] args) {
        Weld weld = new Weld();
        try (WeldContainer container = weld.initialize()) {
            ApplicationRunner runner = container.select(ApplicationRunner.class).get();
            runner.run(args);
        } catch (ApplicationStartupErrorException e) {
            logger.error("Failed to start application", e);
        }
    }

    @Override
    public void run(String... args) throws ApplicationStartupErrorException {
        try (WebServer server = webServer.start()) {
            logger.info("Web server started on port {}", server.getPort());
        } catch (Exception e) {
            throw new ApplicationStartupErrorException("Failed to start web server", e);
        }
    }
}
