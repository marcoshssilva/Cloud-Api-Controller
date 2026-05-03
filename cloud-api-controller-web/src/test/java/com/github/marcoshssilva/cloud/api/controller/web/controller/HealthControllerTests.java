package com.github.marcoshssilva.cloud.api.controller.web.controller;

import com.github.marcoshssilva.cloud.api.controller.core.utils.StandaloneStartupProcess;
import com.github.marcoshssilva.cloud.api.controller.core.utils.WeldContainerHelper;
import com.github.marcoshssilva.cloud.api.controller.web.controllers.HealthController;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpStatusCode;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpResponse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthControllerTests {
    @BeforeAll
    static void setUp() {
        StandaloneStartupProcess.main(new String[]{});
    }

    @Test
    void shouldTestIfHealthStatusIsOk() {
        HealthController healthController = WeldContainerHelper.getContainer().select(HealthController.class).get();
        HttpResponse status = healthController.getStatus();
        assertEquals(HttpStatusCode.OK, status.getStatusCode());
        assertEquals("{ \"status\": \"OK\" }", new String(status.getBody(), StandardCharsets.UTF_8));
    }
}
