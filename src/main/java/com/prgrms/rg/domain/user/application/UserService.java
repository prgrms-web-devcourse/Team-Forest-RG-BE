package com.prgrms.rg.domain.user.application;

import com.prgrms.rg.web.user.results.OAuthLoginResult;
import com.prgrms.rg.web.user.results.UserMeResult;

public interface UserService {
	UserMeResult findUserById(Long id);

	OAuthLoginResult joinOAuth(String authorizationCode) throws Exception;
}
