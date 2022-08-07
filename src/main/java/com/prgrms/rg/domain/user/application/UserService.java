package com.prgrms.rg.domain.user.application;

import java.util.Optional;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.web.user.results.OAuthLoginResult;
import com.prgrms.rg.web.user.results.UserMeResult;

public interface UserService {
	UserMeResult findUserById(Long id);
	OAuthLoginResult joinOAuth(String authorizationCode, String fromUrl) throws Exception;
}
