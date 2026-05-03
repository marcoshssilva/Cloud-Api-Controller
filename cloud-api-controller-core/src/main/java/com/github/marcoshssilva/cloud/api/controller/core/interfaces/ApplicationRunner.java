package com.github.marcoshssilva.cloud.api.controller.core.interfaces;


import com.github.marcoshssilva.cloud.api.controller.core.exceptions.ApplicationStartupErrorException;

public interface ApplicationRunner {
    void run(String... args) throws ApplicationStartupErrorException;
}
