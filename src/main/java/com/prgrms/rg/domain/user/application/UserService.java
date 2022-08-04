package com.prgrms.rg.domain.user.application;

import java.util.Optional;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.web.user.results.OAuthLoginResult;

public interface UserService {
	Optional<User> findUserById(Long id);
	OAuthLoginResult joinOAuth(String authorizationCode) throws Exception;
}
