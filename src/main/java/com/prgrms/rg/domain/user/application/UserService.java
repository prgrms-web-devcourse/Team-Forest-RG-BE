package com.prgrms.rg.domain.user.application;

import java.util.Optional;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.prgrms.rg.domain.user.model.User;

public interface UserService {
	Optional<User> findUserById(Long id);
	User join(OAuth2User oauth2User, String authorizedClientRegistrationId);
}
