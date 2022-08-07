package com.prgrms.rg.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.prgrms.rg.domain.auth.jwt.Jwt;
import com.prgrms.rg.domain.auth.jwt.JwtAuthenticationFilter;
import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
@Profile("security")
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

	private final JwtConfigure jwtConfigure;

	public WebSecurityConfigure(JwtConfigure jwtConfigure) {
		this.jwtConfigure = jwtConfigure;
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/assets/**", "/h2-console/**");
	}

	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, e) -> {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Object principal = authentication != null ? authentication.getPrincipal() : null;
			log.warn("{} is denied", principal, e);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write("ACCESS DENIED");
			response.getWriter().flush();
			response.getWriter().close();
		};
	}

	public Jwt jwt() {
		return new Jwt(
			jwtConfigure.getIssuer(),
			jwtConfigure.getClientSecret(),
			jwtConfigure.getExpirySeconds()
		);
	}

	@Bean
	public JwtTokenProvider jwtTokenProvider() {
		return new JwtTokenProvider(jwt());
	}

	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtConfigure.getHeader(), jwt());
	}

	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("*"); // 나중에 바꾸기
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.cors().configurationSource(corsConfigurationSource())
			.and()
			.authorizeRequests()
			.antMatchers("/user/me").authenticated()
			.anyRequest().permitAll()
			.and()

			/*
			 * formLogin, csrf, headers, http-basic, rememberMe, logout filter 비활성화
			 */
			.formLogin()
			.disable()
			.csrf()
			.disable()
			.headers()
			.disable()
			.httpBasic()
			.disable()
			.rememberMe()
			.disable()
			.logout()
			.disable()
			/*
			 * Session 사용하지 않음
			 */
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			/*
			 * 예외처리 핸들러
			 */
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler())
			.and()
			/*
			 * Jwt 필터
			 */
			.addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class)
		;
	}

}
