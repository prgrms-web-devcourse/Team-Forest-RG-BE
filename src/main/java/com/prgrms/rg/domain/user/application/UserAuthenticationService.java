package com.prgrms.rg.domain.user.application;

import java.io.IOException;

import com.prgrms.rg.domain.user.application.command.UserRegisterCommand;
import com.prgrms.rg.web.user.results.OAuthLoginResult;
import com.prgrms.rg.web.user.results.UserMeResult;
import com.prgrms.rg.web.user.results.UserRegisterResult;

public interface UserAuthenticationService {
	UserMeResult checkUserById(Long id);

	OAuthLoginResult joinOAuth(String authorizationCode) throws IOException, InterruptedException;

	UserRegisterResult updateUserByRegistration(UserRegisterCommand userRegisterCommand);
}
