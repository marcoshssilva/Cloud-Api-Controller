package com.github.marcoshssilva.cloud.api.controller.web;

import com.github.marcoshssilva.cloud.api.controller.web.exceptions.ApplicationStartupErrorException;
import com.github.marcoshssilva.cloud.api.controller.web.exceptions.WebServerError;
import com.github.marcoshssilva.cloud.api.controller.web.utils.LoggerHelper;
import org.apache.logging.log4j.Logger;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;


@Named("AppStartup")
@ApplicationScoped
public class App implements ApplicationRunner {
    public static final Logger logger = LoggerHelper.getLogger(App.class);
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final Thread CURRENT_THREAD = Thread.currentThread();

    private final WebServer webServer;

    public @Inject App(@Named("UndertowServerWebServer") WebServer webServer) {
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
        LocalTime now = LocalTime.now();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                logger.info("Shutting down web server...");
                webServer.stop();
            } catch (WebServerError e) {
                logger.error("Failed to stop web server during shutdown. Cause: {}", e.getMessage(), e);
            }
        }));
        this.printBanner();
        try (WebServer server = webServer.start()) {
            logger.info("Web server started at {} on port {}", server.getHost(), server.getPort());
            logger.info("Management server started at {} on port {}", server.getManagementHost(), server.getManagementPort());
            LocalTime end = LocalTime.now();
            int nano = Duration.between(now, end).getNano();
            long millis = TimeUnit.NANOSECONDS.toMillis(nano);
            logger.info("Server uptime: {} ms", millis);
            CURRENT_THREAD.join();
        } catch (Exception e) {
            throw new ApplicationStartupErrorException("Failed to start web server", e);
        }
    }

    public void printBanner() {
        System.out.println(ANSI_CYAN +
                "  ____ _                 _      _    ____ ___    ____            _             _ _               \n" +
                " / ___| | ___  _   _  __| |    / \\  |  _ \\_ _|  / ___|___  _ __ | |_ _ __ ___ | | | ___ _ __ \n" +
                "| |   | |/ _ \\| | | |/ _` |   / _ \\ | |_) | |  | |   / _ \\| '_ \\| |_| '__/ _ \\| | |/ _ \\ '__|\n" +
                "| |___| | (_) | |_| | (_| |  / ___ \\|  __/| |  | |__| (_) | | | | |_| | | (_) | | |  __/ |   \n" +
                " \\____|_|\\___/ \\__,_|\\__,_| /_/   \\_\\_|  |___|  \\____\\___/|_| |_|\\__|_|  \\___/|_|_|\\___|_|   \n" +
                "" +
                ANSI_RESET);
    }
}
