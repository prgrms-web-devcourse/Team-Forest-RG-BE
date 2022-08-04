package com.prgrms.rg.domain.auth.jwt;

import static org.apache.commons.lang3.ObjectUtils.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

public class JwtAuthenticationFilter extends GenericFilterBean {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final String headerKey;

	private final Jwt jwt;

	public JwtAuthenticationFilter(String headerKey, Jwt jwt) {
		this.headerKey = headerKey;
		this.jwt = jwt;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
		throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			String token = getToken(request);
			if (token != null) {
				try {
					Jwt.Claims claims = verify(token);
					log.debug("Jwt parse result: {}", claims);

					Long userId = claims.userId;
					GrantedAuthority authority = getAuthorities(claims);

					if (isNotEmpty(userId) && authority != null) {
						JwtAuthenticationToken authentication =
							new JwtAuthenticationToken(new JwtAuthentication(token, userId), null, List.of(authority));
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				} catch (Exception e) {
					log.warn("Jwt processing failed: {}", e.getMessage());
				}
			}
		} else {
			log.debug("SecurityContextHolder not populated with security token, as it already contained: '{}'",
				SecurityContextHolder.getContext().getAuthentication());
		}

		chain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request) {
		String token = request.getHeader(headerKey);
		if (isNotEmpty(token)) {
			log.debug("Jwt authorization api detected: {}", token);
			return URLDecoder.decode(token.split(" ")[1], StandardCharsets.UTF_8);
		}
		return null;
	}

	private Jwt.Claims verify(String token) {
		return jwt.verify(token);
	}

	private GrantedAuthority getAuthorities(Jwt.Claims claims) {
		String role = claims.role;
		return role == null ?
			null : new SimpleGrantedAuthority(role);
	}

}
