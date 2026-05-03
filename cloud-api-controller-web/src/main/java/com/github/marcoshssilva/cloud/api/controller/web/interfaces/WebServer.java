package com.github.marcoshssilva.cloud.api.controller.web.interfaces;

import com.github.marcoshssilva.cloud.api.controller.web.data.WebServerStatus;
import com.github.marcoshssilva.cloud.api.controller.web.exceptions.WebServerError;

public interface WebServer extends AutoCloseable {
    WebServer start() throws WebServerError;
    WebServer stop() throws WebServerError;
    WebServerStatus getStatus();
    int getPort();
    int getManagementPort();
    String getHost();
    String getManagementHost();
}
