package com.github.marcoshssilva.cloud.api.controller.web.interfaces;

import jakarta.enterprise.context.NormalScope;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE })
@Retention(RUNTIME)
@Documented
@NormalScope
@Inherited
public @interface HttpController {
    String path() default "/" ;
}
