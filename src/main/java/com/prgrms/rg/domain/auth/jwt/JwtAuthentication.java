package com.prgrms.rg.domain.auth.jwt;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class JwtAuthentication {

	public final String token;

	public final Long userId;

	JwtAuthentication(String token, Long userId) {
		checkArgument(isNotEmpty(token), "token must be provided.");
		checkArgument(isNotEmpty(userId), "userId must be provided.");

		this.token = token;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("token", token)
			.append("username", userId)
			.toString();
	}

}