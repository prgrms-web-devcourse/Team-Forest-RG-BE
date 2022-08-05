package com.prgrms.rg.web.user.requests;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthLoginRequest {
	private String authorizationCode;
	private String fromUrl;
	public OAuthLoginRequest(String authorizationCode, String fromUrl) {
		this.authorizationCode = authorizationCode;
		this.fromUrl = fromUrl;
	}
}
