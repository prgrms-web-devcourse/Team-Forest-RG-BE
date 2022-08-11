package com.prgrms.rg.domain.user.application.impl;

import static com.google.common.base.Preconditions.*;
import static com.prgrms.rg.web.common.message.CriticalMessageSender.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.prgrms.rg.domain.auth.JwtRefreshToken;
import com.prgrms.rg.domain.auth.JwtRefreshTokenRepository;
import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.common.model.metadata.BicycleRepository;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.ridingpost.model.AddressCodeRepository;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.user.application.UserAuthenticationService;
import com.prgrms.rg.domain.user.application.command.UserRegisterCommand;
import com.prgrms.rg.domain.user.model.Introduction;
import com.prgrms.rg.domain.user.model.Manner;
import com.prgrms.rg.domain.user.model.Nickname;
import com.prgrms.rg.domain.user.model.RiderProfile;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;
import com.prgrms.rg.domain.user.model.dto.UserRegisterDTO;
import com.prgrms.rg.infrastructure.oauth.OAuthManager;
import com.prgrms.rg.web.user.requests.UserRegisterRequest;
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
	private final BicycleRepository bicycleRepository;
	private final AddressCodeRepository addressCodeRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final OAuthManager communicator;
	private final JwtRefreshTokenRepository jwtRefreshTokenRepository;

	@Value("${jwt.refresh-expiry-seconds}")
	private long refreshTokenExpiryTime;

	private static final int MILLISECOND_CORRECTION = 1000;

	@Override
	@Transactional
	public UserMeResult checkUserById(Long id, String token) {
		checkArgument(isNotEmpty(id), "id must be provided.");
		User user = userRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Could not found user for userId"));
		DecodedJWT decode = JWT.decode(token);
		String newToken = token;
		if (decode.getExpiresAt().before(new Date())) {
			JwtRefreshToken jwtRefreshToken = jwtRefreshTokenRepository.findByUserId(id)
				.orElseThrow(() -> new NoSuchElementException("해당하는 리프레시 토큰 없음"));
			if (jwtRefreshToken.getExp().before(new Date())) {
				log.info("jwt 만료시간 {}", jwtRefreshToken.getExp());
				jwtRefreshTokenRepository.delete(jwtRefreshToken);
				throw new JWTVerificationException("jwt 만료시간 초과");
			}
			newToken = "Bearer " + jwtTokenProvider.createToken("ROLE_USER", id);
		}
		return UserMeResult.of(user.getNickname(), user.getId(), user.isRegistered(), newToken);
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
				Date now = new Date();
				if (jwtRefreshTokenRepository.findByUserId(user.getId()).isEmpty()) {
					jwtRefreshTokenRepository.save(new JwtRefreshToken(user.getId(), new Date(),
						new Date(now.getTime() + refreshTokenExpiryTime * MILLISECOND_CORRECTION)));
				}
				JwtRefreshToken jwtRefreshToken = jwtRefreshTokenRepository.findByUserId(user.getId())
					.orElseThrow(() -> new NoSuchElementException("토큰을 찾을 수 없습니다."));
				if (jwtRefreshToken.getExp().before(new Date())) {
					jwtRefreshTokenRepository.delete(jwtRefreshToken);
					jwtRefreshTokenRepository.save(new JwtRefreshToken(user.getId(), new Date(),
						new Date(now.getTime() + refreshTokenExpiryTime * MILLISECOND_CORRECTION)));
				}
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
				Date now = new Date();
				jwtRefreshTokenRepository.save(new JwtRefreshToken(user.getId(), new Date(now.getTime()),
					new Date(now.getTime() + refreshTokenExpiryTime * MILLISECOND_CORRECTION)));
				return OAuthLoginResult.of(token, true);
			});
	}

	@Override
	@Transactional
	public UserRegisterResult updateUserByRegistration(UserRegisterCommand userRegisterCommand) {
		User user = userRepository.findById(userRegisterCommand.getUserId())
			.orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
		if (user.isRegistered()) {
			log.info("Already exists: {} user", userRegisterCommand.getUserId());
			return UserRegisterResult.of(false);
		}
		AddressCode addressCode = addressCodeRepository.findByCode(userRegisterCommand.getFavoriteRegionCode())
			.orElseThrow(() -> new NoSuchElementException("없는 주소코드 입니다."));
		UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
			.favoriteRegionCode(addressCode)
			.nickNameAndLevel(userRegisterCommand.getNickName(), userRegisterCommand.getLevel())
			.ridingStartYearAndPhoneNumber(userRegisterCommand.getRidingStartYear(),
				userRegisterCommand.getPhoneNumber())
			.build();
		user.updateByRegistration(userRegisterDTO);
		for (String bicycle : userRegisterCommand.getBicycles())
			user.addBicycle(
				bicycleRepository.findByName(bicycle).orElseThrow(() -> new NoSuchElementException("없는 자전거 종류입니다.")));
		return UserRegisterResult.of(true);
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
			.profile(new RiderProfile(2017, RidingLevel.INTERMEDIATE))
			.build();
		AddressCode save = addressCodeRepository.save(new AddressCode(99999));
		UserRegisterCommand userRegisterCommand = createTestRegisterCommand(admin.getId());
		UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
			.favoriteRegionCode(save)
			.nickNameAndLevel(userRegisterCommand.getNickName(), userRegisterCommand.getLevel())
			.ridingStartYearAndPhoneNumber(userRegisterCommand.getRidingStartYear(),
				userRegisterCommand.getPhoneNumber())
			.build();
		admin.updateByRegistration(userRegisterDTO);
		userRepository.save(admin);
		send(this.generateToken(admin));
	}

	private UserRegisterCommand createTestRegisterCommand(Long userId) {
		List<String> bicycles = new ArrayList<>();
		bicycles.add("MTB");
		return UserRegisterCommand.of(new UserRegisterRequest(1996, 11010, bicycles, "하", "010-1234-5678", "김훈기"),
			userId);
	}

	private String generateToken(User user) {
		return jwtTokenProvider.createToken("ROLE_USER", user.getId());
	}
}
