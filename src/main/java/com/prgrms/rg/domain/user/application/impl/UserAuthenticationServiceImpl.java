package com.prgrms.rg.domain.user.application.impl;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.prgrms.rg.domain.auth.JwtRefreshToken;
import com.prgrms.rg.domain.auth.JwtRefreshTokenRepository;
import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.common.file.model.TemporaryImage;
import com.prgrms.rg.domain.common.file.model.TemporaryImageRepository;
import com.prgrms.rg.domain.common.model.metadata.BicycleRepository;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.ridingpost.model.AddressCodeRepository;
import com.prgrms.rg.domain.user.application.UserAuthenticationService;
import com.prgrms.rg.domain.user.application.command.UserRegisterCommand;
import com.prgrms.rg.domain.user.application.exception.DuplicateNicknameException;
import com.prgrms.rg.domain.user.model.Manner;
import com.prgrms.rg.domain.user.model.Nickname;
import com.prgrms.rg.domain.user.model.RiderProfile;
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
	private static final int MILLISECOND_CORRECTION = 1000;
	private static final int DEFAULT_ADDRESS_CODE = 11010;
	private static final int DEFAULT_RIDDING_START_YEAR = 2022;
	private final UserRepository userRepository;
	private final BicycleRepository bicycleRepository;
	private final AddressCodeRepository addressCodeRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final OAuthManager communicator;
	private final JwtRefreshTokenRepository jwtRefreshTokenRepository;
	private final TemporaryImageRepository temporaryImageRepository;
	@Value("${jwt.refresh-expiry-seconds}")
	private long refreshTokenExpiryTime;

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
		return UserMeResult.of(user.getId(), user.isRegistered(), newToken);
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
				createRefreshToken(user.getId());
				return OAuthLoginResult.of(generateToken(user), false);
			})
			.orElseGet(() -> {
				String nickname = oauthInformation.get("nickname");
				String profileImage = oauthInformation.get("profile_image");

				User user = userRepository.save(User.builder()
					.nickname(new Nickname(nickname))
					.providerId(providerId)
					.provider(provider)
					.manner(Manner.create())
					.isRegistered(false)

					.profile(new RiderProfile(DEFAULT_RIDDING_START_YEAR, RidingLevel.INTERMEDIATE))
					.addressCode(new AddressCode(DEFAULT_ADDRESS_CODE))

					.build());

				TemporaryImage image = temporaryImageRepository.save(new TemporaryImage(profileImage, profileImage));
				user.attach(image);
				Date now = new Date();

				jwtRefreshTokenRepository.save(new JwtRefreshToken(user.getId(), new Date(now.getTime()),
					new Date(now.getTime() + refreshTokenExpiryTime * MILLISECOND_CORRECTION)));

				return OAuthLoginResult.of(generateToken(user), true);
			});
	}

	@Override
	@Transactional
	public UserRegisterResult updateUserByRegistration(UserRegisterCommand userRegisterCommand) {
		User user = userRepository.findById(userRegisterCommand.getUserId())
			.orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
		var nicknameCandidate = new Nickname(userRegisterCommand.getNickName());
		if(userRepository.existsUserByNickname(nicknameCandidate)) {
			throw new DuplicateNicknameException(nicknameCandidate);
		};

		if (user.isRegistered()) {
			log.info("Already exists: {} user", userRegisterCommand.getUserId());
			return UserRegisterResult.of(false);
		}

		AddressCode addressCode = addressCodeRepository.findByCode(userRegisterCommand.getFavoriteRegionCode())
			.orElseGet(() -> addressCodeRepository.findByCode(DEFAULT_ADDRESS_CODE).get());

		UserRegisterDTO userRegisterDTO = createUserRegisterDTO(addressCode, userRegisterCommand.getNickName(),
			userRegisterCommand.getLevel(), userRegisterCommand.getRidingStartYear(),
			userRegisterCommand.getPhoneNumber());

		user.updateByRegistration(userRegisterDTO);

		for (String bicycle : userRegisterCommand.getBicycles())
			bicycleRepository.findByName(bicycle).ifPresent(user::addBicycle);

		return UserRegisterResult.of(true);
	}


	private String generateToken(User user) {
		return jwtTokenProvider.createAdminToken("ROLE_USER", user.getId());
	}

	private UserRegisterDTO createUserRegisterDTO(AddressCode addressCode, String nickName, String ridingLevel,
		int ridingStartYear, String phoneNumber) {
		return UserRegisterDTO.builder()
			.favoriteRegionCode(addressCode)
			.nickNameAndLevel(nickName, ridingLevel)
			.ridingStartYearAndPhoneNumber(ridingStartYear, phoneNumber)
			.build();

	}

	private void createRefreshToken(Long userId) {
		Date now = new Date();
		if (jwtRefreshTokenRepository.findByUserId(userId).isEmpty()) {
			jwtRefreshTokenRepository.save(new JwtRefreshToken(userId, new Date(),
				new Date(now.getTime() + refreshTokenExpiryTime * MILLISECOND_CORRECTION)));
		}
		JwtRefreshToken jwtRefreshToken = jwtRefreshTokenRepository.findByUserId(userId)
			.orElseThrow(() -> new NoSuchElementException("토큰을 찾을 수 없습니다."));

		if (jwtRefreshToken.getExp().before(new Date())) {
			jwtRefreshTokenRepository.delete(jwtRefreshToken);
			jwtRefreshTokenRepository.save(new JwtRefreshToken(userId, new Date(),
				new Date(now.getTime() + refreshTokenExpiryTime * MILLISECOND_CORRECTION)));
		}
	}
}
