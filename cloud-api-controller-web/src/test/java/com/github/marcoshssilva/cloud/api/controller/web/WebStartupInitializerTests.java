package com.github.marcoshssilva.cloud.api.controller.web;

import com.github.marcoshssilva.cloud.api.controller.core.interfaces.WebServer;
import com.github.marcoshssilva.cloud.api.controller.web.impl.UndertowServerWebServerImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class WebStartupInitializerTests {

    @DisplayName("Should start and stop web server with success")
    @Test
    void shouldStartWebServerWithSuccess() throws Exception {
        try(WebServer webServer = new UndertowServerWebServerImpl()) {
            assertDoesNotThrow(() -> {
                webServer.start();
                webServer.stop();
            });
        }
    }
}
