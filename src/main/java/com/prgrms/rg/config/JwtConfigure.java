package com.prgrms.rg.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfigure {

	@Value("${jwt.header}")
	private String header;
	@Value("${jwt.issuer}")
	private String issuer;
	@Value("${jwt.client-secret}")
	private String clientSecret;
	@Value("${jwt.expiry-seconds}")
	private int expirySeconds;

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
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

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("header", header)
			.append("issuer", issuer)
			.append("clientSecret", clientSecret)
			.append("expirySeconds", expirySeconds)
			.toString();
	}

}