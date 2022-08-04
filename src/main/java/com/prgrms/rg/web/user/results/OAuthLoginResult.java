package com.prgrms.rg.web.user.results;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthLoginResult {
	private String accessToken;
	private Boolean isNew;

	public static OAuthLoginResult of(String accessToken, Boolean isNew) {
		OAuthLoginResult result = new OAuthLoginResult();
		result.setAccessToken(accessToken);
		result.setIsNew(isNew);
		return result;
	}
}
