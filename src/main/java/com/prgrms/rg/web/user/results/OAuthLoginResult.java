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
	private String fromUrl;
	public static OAuthLoginResult of(String accessToken, Boolean isNew, String fromUrl) {
		OAuthLoginResult result = new OAuthLoginResult();
		result.setAccessToken(accessToken);
		result.setIsNew(isNew);
		result.setFromUrl(fromUrl);
		return result;
	}
}
