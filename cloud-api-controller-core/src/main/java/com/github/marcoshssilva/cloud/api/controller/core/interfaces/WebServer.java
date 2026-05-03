package com.github.marcoshssilva.cloud.api.controller.core.interfaces;

import com.github.marcoshssilva.cloud.api.controller.core.WebServerStatus;
import com.github.marcoshssilva.cloud.api.controller.core.exceptions.WebServerError;

public interface WebServer extends AutoCloseable {
    WebServer start() throws WebServerError;
    WebServer stop() throws WebServerError;
    WebServerStatus getStatus();
    int getPort();
    int getManagementPort();
    String getHost();
    String getManagementHost();
}
