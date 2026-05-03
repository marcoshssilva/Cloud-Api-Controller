package com.github.marcoshssilva.cloud.api.controller.web.interfaces;

import com.github.marcoshssilva.cloud.api.controller.web.data.HttpMethod;
import jakarta.enterprise.context.NormalScope;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD })
@Retention(RUNTIME)
@Documented
@NormalScope
@Inherited
public @interface HttpOperation {
    HttpMethod method() default HttpMethod.GET;
    String path() default "";
}
