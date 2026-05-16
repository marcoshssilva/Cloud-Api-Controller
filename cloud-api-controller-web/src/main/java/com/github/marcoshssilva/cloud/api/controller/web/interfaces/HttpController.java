package com.github.marcoshssilva.cloud.api.controller.web.interfaces;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Stereotype;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.marcoshssilva.cloud.api.controller.web.data.ServerPort;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE })
@Retention(RUNTIME)
@Documented
@Inherited
@Stereotype
@ApplicationScoped
public @interface HttpController {
    String path() default "/" ;
    int priority() default Integer.MIN_VALUE;
    ServerPort port() default ServerPort.APPLICATION;
}
