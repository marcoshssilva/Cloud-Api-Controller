package com.github.marcoshssilva.cloud.api.controller.web.controllers;

import com.github.marcoshssilva.cloud.api.controller.core.interfaces.Logger;
import com.github.marcoshssilva.cloud.api.controller.core.utils.LoggerHelper;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpHeader;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpMethod;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpStatusCode;
import com.github.marcoshssilva.cloud.api.controller.web.impl.HttpResponseImpl;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpController;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpOperation;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpRequest;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpResponse;

import java.util.Set;

@HttpController(path = "/static", priority = Integer.MIN_VALUE)
public class DefaultController {
    public static final Logger LOG = LoggerHelper.getLogger(DefaultController.class);

    @HttpOperation(method = { HttpMethod.GET, HttpMethod.POST }, path = "/**")
    public HttpResponse fallback(HttpRequest request) {
        String path = request.getPath().replaceFirst("/static", "");
        if (path.isEmpty() || path.equals("/")) {
            path = "index.html";
        } else if (path.startsWith("/")) {
            path = path.substring(1);
        }

        try (java.io.InputStream is = getClass().getClassLoader().getResourceAsStream("static/" + path)) {
            if (is != null) {
                HttpResponseImpl httpResponse = new HttpResponseImpl();
                httpResponse.setBody(is.readAllBytes());
                httpResponse.setStatusCode(HttpStatusCode.OK);
                httpResponse.getHeaders().add(new HttpHeader("Content-Type", Set.of(getContentType(path))));
                return httpResponse;
            }
        } catch (java.io.IOException e) {
            LOG.error("Error reading static file: " + path, e);
        }

        HttpResponseImpl httpResponse = new HttpResponseImpl();
        httpResponse.setStatusCode(HttpStatusCode.NOT_FOUND);
        httpResponse.getHeaders().add(new HttpHeader("Content-Type", Set.of("text/plain")));
        return httpResponse;
    }

    private String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        return "application/octet-stream";
    }
}
