package com.github.marcoshssilva.cloud.api.controller.core.utils;


import com.github.marcoshssilva.cloud.api.controller.core.exceptions.ApplicationStartupErrorException;
import com.github.marcoshssilva.cloud.api.controller.core.interfaces.ApplicationRunner;
import com.github.marcoshssilva.cloud.api.controller.core.interfaces.Logger;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public final class StandaloneStartupProcess {
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final Logger logger = LoggerHelper.getLogger(StandaloneStartupProcess.class);
    private StandaloneStartupProcess() {}

    public static void printBanner() {
        System.out.println(ANSI_CYAN +
                "  ____ _                 _      _    ____ ___    ____            _             _ _               \n" +
                " / ___| | ___  _   _  __| |    / \\  |  _ \\_ _|  / ___|___  _ __ | |_ _ __ ___ | | | ___ _ __ \n" +
                "| |   | |/ _ \\| | | |/ _` |   / _ \\ | |_) | |  | |   / _ \\| '_ \\| |_| '__/ _ \\| | |/ _ \\ '__|\n" +
                "| |___| | (_) | |_| | (_| |  / ___ \\|  __/| |  | |__| (_) | | | | |_| | | (_) | | |  __/ |   \n" +
                " \\____|_|\\___/ \\__,_|\\__,_| /_/   \\_\\_|  |___|  \\____\\___/|_| |_|\\__|_|  \\___/|_|_|\\___|_|   \n" +
                "" +
                ANSI_RESET);
    }

    public static void main(String[] args) {
        StandaloneStartupProcess.printBanner();
        Weld weld = new Weld();
        try (WeldContainer container = weld.initialize()) {
            WeldContainerHelper.setContainer(container);
            ApplicationRunner runner = container.select(ApplicationRunner.class).get();
            runner.run(args);
        } catch (ApplicationStartupErrorException e) {
            logger.error("Failed to start application", e);
        }
    }
}
