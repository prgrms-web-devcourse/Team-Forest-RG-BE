package com.prgrms.rg.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import com.prgrms.rg.domain.auth.jwt.Jwt;
import com.prgrms.rg.domain.auth.jwt.JwtAuthenticationFilter;
import com.prgrms.rg.domain.auth.model.HttpCookieOAuth2AuthorizationRequestRepository;
import com.prgrms.rg.domain.auth.model.OAuth2AuthenticationSuccessHandler;
import com.prgrms.rg.domain.user.application.UserService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
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

	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtConfigure.getHeader(), jwt());
	}

	public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
		return new HttpCookieOAuth2AuthorizationRequestRepository();
	}

	public OAuth2AuthorizedClientService authorizedClientService(
	) {
		JdbcOperations jdbcOperations = getApplicationContext().getBean(JdbcOperations.class);
		ClientRegistrationRepository clientRegistrationRepository = getApplicationContext().getBean(
			ClientRegistrationRepository.class);
		return new JdbcOAuth2AuthorizedClientService(jdbcOperations, clientRegistrationRepository);
	}

	public OAuth2AuthorizedClientRepository authorizedClientRepository() {
		return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService());
	}

	public OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler() {
		return new OAuth2AuthenticationSuccessHandler(jwt(), getApplicationContext().getBean(UserService.class));
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.authorizeRequests()
			.antMatchers("/user/me").hasAnyRole("USER")
			.anyRequest().permitAll()
			.and()
			/**
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
			/**
			 * Session 사용하지 않음
			 */
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			/**
			 * OAuth2 설정
			 */
			.oauth2Login()
			.authorizationEndpoint()
			.authorizationRequestRepository(authorizationRequestRepository())
			.and()
			.successHandler(oauth2AuthenticationSuccessHandler())
			.authorizedClientService(authorizedClientService())
			.authorizedClientRepository(
				authorizedClientRepository())
			.and()
			/**
			 * 예외처리 핸들러
			 */
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler())
			.and()
			/**
			 * Jwt 필터
			 */
			.addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class)
		;
	}

}
