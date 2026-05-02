package com.github.marcoshssilva.cloud.api.controller.web;

import com.github.marcoshssilva.cloud.api.controller.web.exceptions.WebServerError;

public interface WebServer extends AutoCloseable {
    WebServer start() throws WebServerError;
    WebServer stop() throws WebServerError;
    int getPort();
    int getManagementPort();
    String getHost();
    String getManagementHost();

}
