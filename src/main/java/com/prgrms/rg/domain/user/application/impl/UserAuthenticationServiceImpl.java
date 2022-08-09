package com.prgrms.rg.domain.user.application.impl;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.io.IOException;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.user.application.UserAuthenticationService;
import com.prgrms.rg.domain.user.model.Manner;
import com.prgrms.rg.domain.user.model.Nickname;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;
import com.prgrms.rg.infrastructure.oauth.OAuthManager;
import com.prgrms.rg.web.user.results.OAuthLoginResult;
import com.prgrms.rg.web.user.results.UserMeResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final OAuthManager communicator;

	@Override
	@Transactional
	public UserMeResult findUserById(Long id) {
		checkArgument(isNotEmpty(id), "id must be provided.");
		User user = userRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Could not found user for userId"));
		return UserMeResult.of(user.getNickname(), user.getId());
	}

	@Override
	@Transactional
	public OAuthLoginResult joinOAuth(String authorizationCode) throws IOException, InterruptedException {
		checkArgument(authorizationCode != null, "authorizationCode must be provided");
		checkArgument(!authorizationCode.equals(""), "authorizationCode must be provided");
		Map<String, String> oauthInformation = communicator.convertAuthorizationCodeToInfo(authorizationCode);

		String provider = "kakao";
		String providerId = oauthInformation.get("id");

		return userRepository.findByProviderAndProviderId(provider, providerId)
			.map(user -> {
				log.warn("Already exists: {} for (provider: {}, providerId: {})", user, provider, providerId);
				String token = generateToken(user);
				return OAuthLoginResult.of(token, false);
			})
			.orElseGet(() -> {
				String nickname = oauthInformation.get("nickname");
				String profileImage = oauthInformation.get("profile_image");
				User user = userRepository.save(User.builder()
					.nickname(new Nickname(nickname))
					.profileImages(profileImage)
					.providerId(providerId)
					.provider(provider)
					.manner(Manner.create())
					.isRegistered(false)
					.build());
				String token = generateToken(user);
				return OAuthLoginResult.of(token, true);
			});
	}

	private String generateToken(User user) {
		return jwtTokenProvider.createToken("ROLE_USER", user.getId());
	}
}
