package com.github.marcoshssilva.cloud.api.controller.core.utils;

import com.github.marcoshssilva.cloud.api.controller.core.impl.LoggerImpl;
import com.github.marcoshssilva.cloud.api.controller.core.interfaces.Logger;

public final class LoggerHelper {
    private LoggerHelper() {}

    public static Logger getLogger(Class<?> clazz) {
        return new LoggerImpl(clazz);
    }
}
