package com.prgrms.rg.web.user.requests;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthLoginRequest {
	private String authorizationCode;
	public OAuthLoginRequest(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
}
