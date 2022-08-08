package com.prgrms.rg.web.common;

import static org.springframework.core.Ordered.*;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ControllerAdvice
@ResponseBody
@Order(HIGHEST_PRECEDENCE)
public @interface RgControllerAdvice {
	@AliasFor(annotation = ControllerAdvice.class)
	String[] value() default {};

	@AliasFor(annotation = ControllerAdvice.class)
	String[] basePackages() default {};

	@AliasFor(annotation = ControllerAdvice.class)
	Class<?>[] basePackageClasses() default {};

	@AliasFor(annotation = ControllerAdvice.class)
	Class<?>[] assignableTypes() default {};

	@AliasFor(annotation = ControllerAdvice.class)
	Class<? extends Annotation>[] annotations() default {};
}