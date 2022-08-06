package com.prgrms.rg.web.user.results;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMeResult {

	private String token;

	private Long userId;

	public static UserMeResult of(String token, Long userId) {
		UserMeResult result = new UserMeResult();
		result.setToken(token);
		result.setUserId(userId);
		return result;
	}

	public String getToken() {
		return token;
	}

	public Long getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("token", token)
			.append("userId", userId)
			.toString();
	}
}
