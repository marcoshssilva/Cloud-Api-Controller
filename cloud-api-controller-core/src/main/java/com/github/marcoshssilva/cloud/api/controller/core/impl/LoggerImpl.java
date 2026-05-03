package com.github.marcoshssilva.cloud.api.controller.core.impl;


import com.github.marcoshssilva.cloud.api.controller.core.interfaces.Logger;

public class LoggerImpl implements Logger {
    private final  org.apache.logging.log4j.Logger logger;
    private final Class<?> clazz;


    public LoggerImpl(Class<?> clazz) {
        this.clazz  = clazz;
        this.logger = org.apache.logging.log4j.LogManager.getLogger(clazz);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void info(String message, String... args) {
        logger.info(message, (Object[]) args);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public void error(String message, Exception e) {
        logger.error(message, e);
    }

    @Override
    public void error(String message, Exception e, String... args) {
        Object[] params = new Object[args.length + 1];
        System.arraycopy(args, 0, params, 0, args.length);
        params[args.length] = e;
        logger.error(message, params);
    }

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void debug(String message, String... args) {
        logger.debug(message, (Object[]) args);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void warn(String message, String... args) {
        logger.warn(message, (Object[]) args);
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }
}
