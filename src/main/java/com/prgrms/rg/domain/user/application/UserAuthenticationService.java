package com.prgrms.rg.domain.user.application;

import java.io.IOException;

import com.prgrms.rg.web.user.results.OAuthLoginResult;
import com.prgrms.rg.web.user.results.UserMeResult;

public interface UserAuthenticationService {
	UserMeResult findUserById(Long id);

	OAuthLoginResult joinOAuth(String authorizationCode) throws IOException, InterruptedException;
}
