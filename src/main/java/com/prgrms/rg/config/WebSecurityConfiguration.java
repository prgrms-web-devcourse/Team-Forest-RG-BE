package com.prgrms.rg.config;

import com.prgrms.rg.domain.auth.jwt.Jwt;
import com.prgrms.rg.domain.auth.jwt.JwtAuthenticationFilter;
import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Slf4j
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final JwtConfiguration jwtConfiguration;

  public WebSecurityConfiguration(JwtConfiguration jwtConfiguration) {
    this.jwtConfiguration = jwtConfiguration;
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/assets/**", "/h2-console/**");
  }

  public Jwt jwt() {
    return new Jwt(
        jwtConfiguration.getIssuer(),
        jwtConfiguration.getClientSecret(),
        jwtConfiguration.getExpirySeconds()
    );
  }

  @Bean
  public JwtTokenProvider jwtTokenProvider() {
    return new JwtTokenProvider(jwt());
  }

  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(jwtConfiguration.getHeader(), jwt());
  }

  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(List.of("https://cool-dusk-ced14a.netlify.app", "http://localhost:[*]", "http://127.0.0.1:[*]"));
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(true);
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
        .anyRequest().permitAll()
        .and()
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
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class)
    ;
  }

}
