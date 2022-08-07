package com.prgrms.rg.testutil;

import static org.springframework.context.annotation.ComponentScan.*;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.AliasFor;

import com.prgrms.rg.config.JpaConfiguration;
import com.prgrms.rg.config.JwtConfigure;
import com.prgrms.rg.web.global.message.LocalSystemExceptionMessageSender;

/**
 * <p>Database, Jpa 관련 Bean들을 필요로 하지 않는 Controller 테스트를 작성하기 위한 annotation 입니다.</p>
 * <p>원하는 테스트 대상 컨트롤러를 명시해야 제대로 작동합니다.</p>
 * <p>명시하지 않을 경우 모든 컨트롤러를 로드하기 때문에 의존성 문제가 생길 수 있습니다.</p>
 * <p>&#064;ControllerAdvice  가 붙은 클래스들은 자동으로 등록됩니다.</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@WebMvcTest(
	excludeAutoConfiguration = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class
	},
	includeFilters = {@Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = {
			JwtConfigure.class,
			LocalSystemExceptionMessageSender.class})},
	excludeFilters = {@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JpaConfiguration.class)})
public @interface ControllerTest {
	@AliasFor(annotation = WebMvcTest.class, attribute = "controllers")
	Class<?>[] controllers() default {};

}
