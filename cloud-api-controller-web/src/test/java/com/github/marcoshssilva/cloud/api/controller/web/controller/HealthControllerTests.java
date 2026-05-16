package com.github.marcoshssilva.cloud.api.controller.web.controller;

import com.github.marcoshssilva.cloud.api.controller.core.utils.StandaloneStartupProcess;
import com.github.marcoshssilva.cloud.api.controller.core.utils.WeldContainerHelper;
import com.github.marcoshssilva.cloud.api.controller.web.controllers.HealthController;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpStatusCode;
import com.github.marcoshssilva.cloud.api.controller.web.exceptions.WebServerError;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpResponse;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.WebServer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthControllerTests {
    static WebServer server;

    @BeforeAll
    static void beforeAll() {
        StandaloneStartupProcess.main(new String[0]);
        server = WeldContainerHelper.getContainer().select(WebServer.class).get();
    }

    @AfterAll
    static void afterAll() throws WebServerError {
        server.stop();
    }

    @Test
    void shouldTestIfHealthStatusIsOk() {
        HealthController healthController = WeldContainerHelper.getContainer().select(HealthController.class).get();
        HttpResponse status = healthController.getStatus();
        assertEquals(HttpStatusCode.OK, status.getStatusCode());
        assertEquals("{ \"status\": \"OK\" }", new String(status.getBody(), StandardCharsets.UTF_8));
    }
}
