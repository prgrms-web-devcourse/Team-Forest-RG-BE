package com.prgrms.rg.domain.auth.jwt;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * Jwt Token을 생성, 인증, 권한 부여, 유효성 검사, PK 추출
 */
public class JwtTokenProvider {

	private static final long TOKEN_VALID_MILLISECOND = 1000L * 60 * 60 * 10; // 10시간
	private final Jwt jwt;
	private final String secretKey;

	public JwtTokenProvider(Jwt jwt
	) {
		this.jwt = jwt;
		this.secretKey = jwt.getClientSecret();
	}

	// Jwt 토큰 생성
	public String createToken(String role, Long userId) {
		Claims claims = Jwts.claims();
		claims.put("role", role);
		claims.put("userId", userId);
		Date now = new Date();
		return JWT.create().withExpiresAt(new Date(now.getTime() + TOKEN_VALID_MILLISECOND))
			.withIssuedAt(now)
			.withClaim("role", role)
			.withClaim("userId", userId)
			.withIssuer(jwt.getIssuer())
			.sign(Algorithm.HMAC512(secretKey));
	}

	public String resolveToken(HttpServletRequest req) {
		return req.getHeader("X-AUTH-TOKEN");
	}

}
