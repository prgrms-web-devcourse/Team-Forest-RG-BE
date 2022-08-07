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
	private String username;

	public static UserMeResult of(String username, Long userId) {
		UserMeResult result = new UserMeResult();
		result.setUserId(userId);
		result.setUsername(username);
		return result;
	}

	public Long getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("userId", userId)
			.append("username", username)
			.toString();
	}
}