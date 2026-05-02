package com.github.marcoshssilva.cloud.api.controller.web.utils;

import org.apache.logging.log4j.Logger;

public final class LoggerHelper {
    private LoggerHelper() {}

    public static Logger getLogger(Class<?> clazz) {
        return org.apache.logging.log4j.LogManager.getLogger(clazz);
    }
}
