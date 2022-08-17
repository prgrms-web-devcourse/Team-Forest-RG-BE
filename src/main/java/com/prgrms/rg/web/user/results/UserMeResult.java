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

	private Long userId;
	private boolean isRegistered;
	private String accessToken;

	public static UserMeResult of(Long userId, boolean isRegistered, String token) {
		UserMeResult result = new UserMeResult();
		result.setUserId(userId);
		result.setRegistered(isRegistered);
		result.setAccessToken(token);
		return result;
	}

	public Long getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("userId", userId)
			.toString();
	}
}