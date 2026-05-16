package com.github.marcoshssilva.cloud.api.controller.web.impl;

import com.github.marcoshssilva.cloud.api.controller.core.interfaces.Logger;
import com.github.marcoshssilva.cloud.api.controller.core.utils.LoggerHelper;
import com.github.marcoshssilva.cloud.api.controller.core.utils.WeldContainerHelper;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpMethod;
import com.github.marcoshssilva.cloud.api.controller.web.data.HttpStatusCode;
import com.github.marcoshssilva.cloud.api.controller.web.data.ServerPort;
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
        ServerPort requestServerPort = request.getPort();

        for (Bean<?> bean : controllers) {
            Class<?> beanClass = bean.getBeanClass();
            HttpController controllerAnnotation = beanClass.getAnnotation(HttpController.class);
            String basePath = controllerAnnotation.path();

            if (controllerAnnotation.port() != requestServerPort) {
                continue;
            }
            

            for (Method method : beanClass.getMethods()) {
                if (!method.isAnnotationPresent(HttpOperation.class)) {
                    continue;
                }
                
                HttpOperation operationAnnotation = method.getAnnotation(HttpOperation.class);
                String fullPath = (basePath + operationAnnotation.path()).replaceAll("//+", "/");
                boolean isWildcard = fullPath.endsWith("/**");
                String regexPath = fullPath.replaceAll("/:[^/]+", "/([^/]+)").replaceAll("/\\*\\*", "(/.*)?");

                boolean matches = isWildcard ? requestPath.matches(regexPath) : requestPath.equals(fullPath.replaceAll("/:[^/]+", "/([^/]+)"));
                if (!matches && isWildcard) {
                    String prefix = regexPath.replace("(/.*)?", "");
                    matches = requestPath.startsWith(prefix);
                }

                if (matches) {
                    if (requestMethod == HttpMethod.HEAD) {
                        return buildEmptyResponse(HttpStatusCode.OK);
                    }

                    if (Arrays.stream(operationAnnotation.method()).anyMatch(m -> m == requestMethod)) {
                        return invokeMethod(beanClass, method, request, fullPath, requestPath);
                    }

                    if (Arrays.stream(operationAnnotation.method()).noneMatch(m -> m == requestMethod)) {
                        return buildEmptyResponse(HttpStatusCode.METHOD_NOT_ALLOWED);
                    }
                }
            }
        }

        return buildNotFound();
    }

    private HttpResponse invokeMethod(Class<?> beanClass, Method method, HttpRequest request, String pathTemplate, String requestPath) {
        try {
            Object instance = getControllerInstance(beanClass);
            java.lang.reflect.Parameter[] parameters = method.getParameters();
            Object[] args = new Object[parameters.length];

            // Extract path variables
            String[] templateParts = pathTemplate.split("/");
            String[] pathParts = requestPath.split("/");
            java.util.Map<String, String> pathVariables = new java.util.HashMap<>();
            for (int i = 0; i < templateParts.length; i++) {
                if (templateParts[i].startsWith(":")) {
                    pathVariables.put(templateParts[i].substring(1), i < pathParts.length ? pathParts[i] : "");
                }
            }

            for (int i = 0; i < parameters.length; i++) {
                java.lang.reflect.Parameter parameter = parameters[i];
                if (parameter.isAnnotationPresent(com.github.marcoshssilva.cloud.api.controller.web.interfaces.RequestBody.class)) {
                    Class<?> type = parameter.getType();
                    if (type == String.class) {
                        args[i] = request.getBodyAsString();
                    } else if (type == byte[].class) {
                        args[i] = request.getBody();
                    } else if (type == java.io.InputStream.class) {
                        args[i] = new java.io.ByteArrayInputStream(request.getBody());
                    } else {
                        args[i] = request.getBodyAsString(); // Default fallback
                    }
                } else if (parameter.isAnnotationPresent(com.github.marcoshssilva.cloud.api.controller.web.interfaces.PathVariable.class)) {
                    com.github.marcoshssilva.cloud.api.controller.web.interfaces.PathVariable pv = parameter.getAnnotation(com.github.marcoshssilva.cloud.api.controller.web.interfaces.PathVariable.class);
                    args[i] = pathVariables.get(pv.value());
                } else if (parameter.isAnnotationPresent(com.github.marcoshssilva.cloud.api.controller.web.interfaces.QueryParam.class)) {
                    com.github.marcoshssilva.cloud.api.controller.web.interfaces.QueryParam queryParam = parameter.getAnnotation(com.github.marcoshssilva.cloud.api.controller.web.interfaces.QueryParam.class);
                    args[i] = request.getQueryParameters().stream()
                            .filter(qp -> qp.name().equals(queryParam.value()))
                            .map(com.github.marcoshssilva.cloud.api.controller.web.data.HttpQueryParam::value)
                            .findFirst()
                            .orElse(queryParam.defaultValue());
                } else if (com.github.marcoshssilva.cloud.api.controller.web.interfaces.HttpRequest.class.isAssignableFrom(parameter.getType())) {
                    args[i] = request;
                }
            }

            Object result = method.invoke(instance, args);
            
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
                .sorted((b1, b2) -> {
                    HttpController c1 = b1.getBeanClass().getAnnotation(HttpController.class);
                    HttpController c2 = b2.getBeanClass().getAnnotation(HttpController.class);
                    return Integer.compare(c2.priority(), c1.priority());
                })
                .toList();
    }

    private static Object getControllerInstance(Class<?> beanClass) {
        return WeldContainerHelper.getContainer().select(beanClass).get();
    }
}
