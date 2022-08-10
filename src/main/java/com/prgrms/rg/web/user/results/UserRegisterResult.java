package com.prgrms.rg.web.user.results;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRegisterResult {
	boolean isRegistered;

	public static UserRegisterResult of(boolean isRegistered) {
		UserRegisterResult result = new UserRegisterResult();
		result.setRegistered(isRegistered);
		return result;
	}
}
