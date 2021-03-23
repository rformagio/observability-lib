package com.github.rformagio.observability.annotation;

import com.github.rformagio.observability.config.ConfigApplication;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({ConfigApplication.class})
public @interface EnableObservabiltyLib {
}
