package com.github.marcoshssilva.cloud.api.controller.web.impl;

import com.github.marcoshssilva.cloud.api.controller.core.interfaces.Logger;
import com.github.marcoshssilva.cloud.api.controller.core.utils.LoggerHelper;
import com.github.marcoshssilva.cloud.api.controller.core.utils.WeldContainerHelper;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpMethod;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpStatusCode;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpController;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpOperation;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpRequest;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpRequestProcessor;
import com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
@Named("HttpRequestProcessor")
public class HttpRequestProcessorImpl implements HttpRequestProcessor {
    private static final Logger logger = LoggerHelper.getLogger(HttpRequestProcessorImpl.class);
    private final List<Bean<?>> controllers;

    public HttpRequestProcessorImpl() {
        this(getBeanManagerFromContainer());
    }

    @Inject
    public HttpRequestProcessorImpl(BeanManager beanManager) {
        this.controllers = getControllers(beanManager);
        this.controllers.forEach(beanClass -> logger.debug("Found bean class: {}", beanClass.getBeanClass().getName()));
    }

    @Override
    public HttpResponse process(HttpRequest request) {
        logger.info("Receive request {} - {}", request.getPath(), request.getMethod().name());

        String requestPath = request.getPath();
        HttpMethod requestMethod = request.getMethod();

        for (Bean<?> bean : controllers) {
            Class<?> beanClass = bean.getBeanClass();
            HttpController controllerAnnotation = beanClass.getAnnotation(HttpController.class);
            String basePath = controllerAnnotation.path();

            for (Method method : beanClass.getMethods()) {
                if (!method.isAnnotationPresent(HttpOperation.class)) {
                    continue;
                }
                
                HttpOperation operationAnnotation = method.getAnnotation(HttpOperation.class);
                String fullPath = (basePath + operationAnnotation.path()).replaceAll("//+", "/");
                
                if (fullPath.equals(requestPath)) {
                    if (requestMethod == HttpMethod.HEAD) {
                        return buildEmptyResponse(HttpStatusCode.OK);
                    }

                    if (Arrays.stream(operationAnnotation.method()).anyMatch(m -> m == requestMethod)) {
                        return invokeMethod(beanClass, method);
                    }

                    return buildEmptyResponse(HttpStatusCode.METHOD_NOT_ALLOWED);
                }
            }
        }

        return buildNotFound();
    }

    private HttpResponse invokeMethod(Class<?> beanClass, Method method) {
        try {
            Object instance = getControllerInstance(beanClass);
            Object result = method.invoke(instance);
            
            if (result instanceof HttpResponse httpResponse) {
                return httpResponse;
            } else {
                logger.error("Method {} did not return HttpResponse", new Exception("Invalid Return Type"), method.getName());
                return buildInternalServerError();
            }
        } catch (ReflectiveOperationException | IllegalArgumentException e) {
            logger.error("Error executing HTTP operation", e);
            return buildInternalServerError();
        }
    }
    
    private HttpResponse buildNotFound() {
        return buildEmptyResponse(HttpStatusCode.NOT_FOUND);
    }

    private HttpResponse buildInternalServerError() {
        return buildEmptyResponse(HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
    
    private HttpResponse buildEmptyResponse(HttpStatusCode statusCode) {
        return new HttpResponseImpl(List.of(), List.of(), new byte[0], statusCode);
    }

    private static BeanManager getBeanManagerFromContainer() {
        if (WeldContainerHelper.getContainer() == null) {
            return null;
        }

        return WeldContainerHelper.getContainer().getBeanManager();
    }

    private static List<Bean<?>> getControllers(BeanManager beanManager) {
        if (beanManager == null) {
            return List.of();
        }
        return beanManager.getBeans(Object.class, new AnnotationLiteral<Any>() {}).stream()
                .filter(bean -> bean.getBeanClass().isAnnotationPresent(HttpController.class))
                .toList();
    }

    private static Object getControllerInstance(Class<?> beanClass) {
        return WeldContainerHelper.getContainer().select(beanClass).get();
    }
}
