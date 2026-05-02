package com.github.marcoshssilva.cloud.api.controller.web;

import com.github.marcoshssilva.cloud.api.controller.web.exceptions.ApplicationStartupErrorException;

public interface ApplicationRunner {
    void run(String... args) throws ApplicationStartupErrorException;
}
