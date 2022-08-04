package com.prgrms.rg.domain.auth.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public final class Jwt {

	private final String issuer;

	private final String clientSecret;

	private final int expirySeconds;

	private final Algorithm algorithm;

	private final JWTVerifier jwtVerifier;

	public Jwt(String issuer, String clientSecret, int expirySeconds) {
		this.issuer = issuer;
		this.clientSecret = clientSecret;
		this.expirySeconds = expirySeconds;
		this.algorithm = Algorithm.HMAC512(clientSecret);
		this.jwtVerifier = com.auth0.jwt.JWT
			.require(algorithm)
			.withIssuer(issuer)
			.withClaimPresence("userId")
			.withClaimPresence("role")
			.withClaimPresence("iat")
			.withClaimPresence("exp")
			.build();
	}

	public String sign(Claims claims) {
		Date now = new Date();
		JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
		builder.withIssuer(issuer);
		builder.withIssuedAt(now);
		if (expirySeconds > 0) {
			builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1_000L));
		}
		builder.withClaim("userId", claims.userId);
		builder.withClaim("role", claims.role);
		builder.withSubject(String.valueOf(claims.userId));
		return builder.sign(algorithm);
	}

	public Claims verify(String token) throws JWTVerificationException {
		return new Claims(jwtVerifier.verify(token));
	}

	public String getIssuer() {
		return issuer;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public int getExpirySeconds() {
		return expirySeconds;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public JWTVerifier getJwtVerifier() {
		return jwtVerifier;
	}

	static public class Claims {
		Long userId;
		String role;
		Date iat;
		Date exp;

		private Claims() {/*no-op*/}

		Claims(DecodedJWT decodedJWT) {
			Claim userId = decodedJWT.getClaim("userId");
			if (!userId.isNull())
				this.userId = userId.asLong();
			Claim role = decodedJWT.getClaim("role");
			if (!role.isNull()) {
				this.role = role.asString();
			}
			this.iat = decodedJWT.getIssuedAt();
			this.exp = decodedJWT.getExpiresAt();
		}

		public static Claims of(Long userId, String role) {
			Claims claims = new Claims();
			claims.userId = userId;
			claims.role = role;
			return claims;
		}

		public Map<String, Object> asMap() {
			Map<String, Object> map = new HashMap<>();
			map.put("user_id", userId);
			map.put("role", role);
			map.put("iat", iat());
			map.put("exp", exp());
			return map;
		}

		long iat() {
			return iat != null ? iat.getTime() : -1;
		}

		long exp() {
			return exp != null ? exp.getTime() : -1;
		}

		void eraseIat() {
			iat = null;
		}

		void eraseExp() {
			exp = null;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("userId", userId)
				.append("role", role)
				.append("iat", iat)
				.append("exp", exp)
				.toString();
		}
	}

}
