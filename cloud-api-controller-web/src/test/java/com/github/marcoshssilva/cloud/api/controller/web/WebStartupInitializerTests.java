package com.github.marcoshssilva.cloud.api.controller.web;

import com.github.marcoshssilva.cloud.api.controller.core.utils.StandaloneStartupProcess;
import com.github.marcoshssilva.cloud.api.controller.core.utils.WeldContainerHelper;
import com.github.marcoshssilva.cloud.api.controller.web.data.WebServerStatus;
import com.github.marcoshssilva.cloud.api.controller.web.exceptions.WebServerError;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.WebServer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WebStartupInitializerTests {
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

    @DisplayName("Should test if app run with health check OK")
    @Test
    void shouldTestIfAppRunWithHealthCheckOK() throws Exception {
        HttpClient client   = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new java.net.URI("http://localhost:8081/health/status"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(WebServerStatus.RUNNING, server.getStatus());
        assertEquals(200, response.statusCode());
        assertEquals("{ \"status\": \"OK\" }", response.body());
    }
}
