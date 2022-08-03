package com.prgrms.rg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.prgrms.rg.web.LoggingInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggingInterceptor())
			.order(1)
			.addPathPatterns("/**");
	}

	// @Override
	// public void addCorsMappings(CorsRegistry registry) {
	// 	registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowCredentials(true);
	// }
}
