package com.github.marcoshssilva.cloud.api.controller.core.interfaces;

public interface Logger {
    Class<?> getClazz();
    void info(String message);
    void info(String message, String... args);
    void error(String message);
    void error(String message, Exception e);
    void error(String message, Exception e, String... args);
    void debug(String message);
    void debug(String message, String... args);

    void warn(String message);
    void warn(String message, String... args);
}
