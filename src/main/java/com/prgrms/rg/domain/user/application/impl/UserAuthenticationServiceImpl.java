package com.prgrms.rg.domain.user.application.impl;

import static com.google.common.base.Preconditions.*;
import static com.prgrms.rg.infrastructure.cloud.CriticalMessageSender.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.user.application.UserAuthenticationService;
import com.prgrms.rg.domain.user.application.command.UserRegisterCommand;
import com.prgrms.rg.domain.user.model.Introduction;
import com.prgrms.rg.domain.user.model.Manner;
import com.prgrms.rg.domain.user.model.Nickname;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;
import com.prgrms.rg.domain.user.model.dto.UserRegisterDTO;
import com.prgrms.rg.infrastructure.oauth.OAuthManager;
import com.prgrms.rg.web.user.results.OAuthLoginResult;
import com.prgrms.rg.web.user.results.UserMeResult;
import com.prgrms.rg.web.user.results.UserRegisterResult;

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
	public UserMeResult checkUserById(Long id) {
		checkArgument(isNotEmpty(id), "id must be provided.");
		User user = userRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Could not found user for userId"));
		return UserMeResult.of(user.getNickname(), user.getId(), user.isRegistered());
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

	@Override
	public UserRegisterResult updateUserByRegistration(UserRegisterCommand userRegisterCommand) {
		User user = userRepository.findById(userRegisterCommand.getUserId())
			.orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
		if (user.isRegistered()) {
			log.info("Already exists: {} user", userRegisterCommand.getUserId());
			return UserRegisterResult.of(false);
		}
		UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
			.bicycles(userRegisterCommand.getBicycles())
			.favoriteRegionCode(userRegisterCommand.getFavoriteRegionCode())
			.nickNameAndLevel(userRegisterCommand.getNickName(), userRegisterCommand.getLevel())
			.ridingStartYearAndPhoneNumber(userRegisterCommand.getRidingStartYear(),
				userRegisterCommand.getPhoneNumber())
			.build();
		user.updateByRegistration(userRegisterDTO);
		return UserRegisterResult.of(true);
	}

	private String generateToken(User user) {
		return jwtTokenProvider.createToken("ROLE_USER", user.getId());
	}

	@PostConstruct
	public void init() throws Exception {
		User admin = User.builder()
			.nickname(new Nickname("adminNickname"))
			.manner(Manner.create())
			.isRegistered(true)
			.introduction(new Introduction("관리자입니다."))
			.provider("kakao")
			.providerId("provider_id")
			.build();
		userRepository.save(admin);
		send(this.generateToken(admin));
	}
}
