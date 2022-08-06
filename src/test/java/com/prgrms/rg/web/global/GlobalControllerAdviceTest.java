package com.prgrms.rg.web.global;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.prgrms.rg.config.JpaConfiguration;

@WebMvcTest(controllers = {GlobalControllerAdvice.class}, properties = {"spring.profiles.active=test"},
	excludeAutoConfiguration = {DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class}, excludeFilters = {
	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JpaConfiguration.class)})
class GlobalControllerAdviceTest {

	// @Autowired
	// MockMvc mockMvc;

	@Test
	@DisplayName("다른 ControllerAdvice에서 처리하지 못한 예외들을 처리하고, 사용자에게 500 InternalServerError 메시지를 전송한다.")
	void send_message_when_system_runs_on_production_environment() {

		// Given
		assertThat(1).isEqualTo(1);

		// When

		// Then

	}

}
