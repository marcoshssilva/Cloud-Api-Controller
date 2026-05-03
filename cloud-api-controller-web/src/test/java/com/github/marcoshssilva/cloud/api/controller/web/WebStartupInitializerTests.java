package com.github.marcoshssilva.cloud.api.controller.web;

import com.github.marcoshssilva.cloud.api.controller.core.utils.StandaloneStartupProcess;
import com.github.marcoshssilva.cloud.api.controller.core.utils.WeldContainerHelper;
import com.github.marcoshssilva.cloud.api.controller.web.data.WebServerStatus;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.WebServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WebStartupInitializerTests {

    @DisplayName("Should test if app run with health check OK")
    @Test
    void shouldTestIfAppRunWithHealthCheckOK() throws Exception {
        StandaloneStartupProcess.main(new String[0]);
        WebServer server = WeldContainerHelper.getContainer().select(WebServer.class).iterator().next();
        assertEquals(WebServerStatus.RUNNING, server.getStatus());

        HttpClient client   = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new java.net.URI("http://localhost:8080/health/status"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("{ \"status\": \"OK\" }", response.body());
    }
}
