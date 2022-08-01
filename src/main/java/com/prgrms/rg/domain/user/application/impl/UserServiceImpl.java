package com.prgrms.rg.domain.user.application.impl;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.prgrms.rg.domain.user.application.UserService;
import com.prgrms.rg.domain.user.model.Nickname;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
	@Transactional
	public Optional<User> findUserById(Long id) {
		checkArgument(isNotEmpty(id), "id must be provided.");
		return userRepository.findById(id);
	}

	@Override
	@Transactional
	public User join(OAuth2User oauth2User, String authorizedClientRegistrationId) {
		checkArgument(oauth2User != null, "oauth2User must be provided.");
		checkArgument(isNotEmpty(authorizedClientRegistrationId), "authorizedClientRegistrationId must be provided.");

		String providerId = oauth2User.getName();
		return userRepository.findByProviderAndProviderId(authorizedClientRegistrationId, providerId)
			.map(user -> {
				log.warn("Already exists: {} for (provider: {}, providerId: {})", user, authorizedClientRegistrationId, providerId);
				return user;
			})
			.orElseGet(() -> {
				Map<String, Object> attributes = oauth2User.getAttributes();
				@SuppressWarnings("unchecked")
				Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
				checkArgument(properties != null, "OAuth2User properties is empty");

				String nickname = (String) properties.get("nickname");
				String profileImage = (String) properties.get("profile_image");
				return userRepository.save(User.builder().nickname(new Nickname(nickname)).profileImage(profileImage).providerId(providerId).provider(authorizedClientRegistrationId).build());
			});
	}
}
