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

/**
 * 공용 ControllerAdvice 설정이 개별 ControllerAdvice를 덮어 쓰지 않게끔
 * 우선 순위를 조절한 Annotation입니다. 개별 API 개발시에는 이 애노테이션을 이용하여
 * RestControllerAdvice로 등록해 주시면 됩니다.
 */
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