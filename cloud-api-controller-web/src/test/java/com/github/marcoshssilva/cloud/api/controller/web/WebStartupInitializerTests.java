package com.github.marcoshssilva.cloud.api.controller.web;

import com.github.marcoshssilva.cloud.api.controller.web.data.WebServerStatus;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.WebServer;
import com.github.marcoshssilva.cloud.api.controller.web.impl.UndertowServerWebServerImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WebStartupInitializerTests {

    @DisplayName("Should start and stop web server with success")
    @Test
    void shouldStartWebServerWithSuccess() throws Exception {
        try(WebServer webServer = new UndertowServerWebServerImpl()) {
            assertDoesNotThrow(webServer::start);
            assertEquals(WebServerStatus.RUNNING, webServer.getStatus());
            assertDoesNotThrow(webServer::stop);
            assertEquals(WebServerStatus.STOPPED, webServer.getStatus());
        }
    }
}
