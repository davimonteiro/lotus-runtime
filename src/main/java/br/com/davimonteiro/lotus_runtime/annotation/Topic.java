package br.com.davimonteiro.lotus_runtime.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.engio.mbassy.listener.Handler;

@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
@Target(value = {ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Handler
public @interface Topic {

}
