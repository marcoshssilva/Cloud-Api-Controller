package com.github.marcoshssilva.cloud.api.controller.web.controllers;

import com.github.marcoshssilva.cloud.api.controller.web.data.HttpMethod;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpStatusCode;
import com.github.marcoshssilva.cloud.api.controller.web.data.ServerPort;
import com.github.marcoshssilva.cloud.api.controller.web.impl.HttpResponseImpl;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpController;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpOperation;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.util.List;

@HttpController(path = "/health", port = ServerPort.MANAGEMENT)
public class HealthController {
    @HttpOperation(method = HttpMethod.GET, path = "/status")
    public HttpResponse getStatus() {
        return new HttpResponseImpl(List.of(), List.of(), "{ \"status\": \"OK\" }".getBytes(StandardCharsets.UTF_8), HttpStatusCode.OK);
    }
}
