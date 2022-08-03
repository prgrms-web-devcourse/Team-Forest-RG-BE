package com.prgrms.rg.domain.user.application.impl;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.Transactional;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.rg.config.JwtConfigure;
import com.prgrms.rg.domain.auth.jwt.Jwt;
import com.prgrms.rg.domain.user.application.UserService;
import com.prgrms.rg.domain.user.model.Nickname;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;
import com.prgrms.rg.web.user.results.OAuthLoginResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final JwtConfigure jwtConfigure;

	@Override
	@Transactional
	public Optional<User> findUserById(Long id) {
		checkArgument(isNotEmpty(id), "id must be provided.");
		return userRepository.findById(id);
	}


	@Override
	@Transactional
	public OAuthLoginResult joinOAuth(String authorizationCode) throws Exception {
		checkArgument(authorizationCode != null, "authorizationCode must be provided");
		String accessToken = convertAuthorizationCodeToAccessToken(authorizationCode);
		ConcurrentHashMap<String, String> oauthInformation = convertAccessTokenToOAuthInformation(accessToken);
		// 유저 있는지 확인
		String provider = "kakao";
		String providerId = oauthInformation.get("id");


		return userRepository.findByProviderAndProviderId(provider, providerId)
			.map(user -> { // 이미 유저가 있다면
				log.warn("Already exists: {} for (provider: {}, providerId: {})", user, provider,
					providerId);
				String token = generateToken(user);
				return OAuthLoginResult.of(token,true);
			})
			.orElseGet(() -> { // 없다면
				@SuppressWarnings("unchecked")
				String nickname = oauthInformation.get("nickname");
				String profileImage = oauthInformation.get("profile_image");
				User user = userRepository.save(User.builder()
					.nickname(new Nickname(nickname))
					.profileImage(profileImage)
					.providerId(providerId)
					.provider(provider)
					.build());
				String token = generateToken(user);
				return OAuthLoginResult.of(token,false);
			});
	}

	private String convertAuthorizationCodeToAccessToken(String authorizationCode) throws
		IOException,
		InterruptedException {
		// access 토큰 받는 요청 보내기 (POST)
		var urlEncodedBody = new String(new UrlEncodedFormEntity(
			List.of(
				new BasicNameValuePair("client_id", "8f248aa7874df072e8d15b2d0b284108"),
				new BasicNameValuePair("client_secret", "tbGLY0lEfvxkrgFWfssEaXpWTS73nPJa"),
				new BasicNameValuePair("code", authorizationCode),
				new BasicNameValuePair("grant_type", "authorization_code"),
				new BasicNameValuePair("redirect_url", "http://192.168.219.108:3000/login")
			)
		).getContent().readAllBytes());

		HttpRequest request = HttpRequest
			.newBuilder(URI.create("https://kauth.kakao.com/oauth/token"))
			.POST(HttpRequest.BodyPublishers.ofString(urlEncodedBody))
			.header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
			.build();

		HttpClient client = HttpClient.newHttpClient();
		var response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		return new ObjectMapper().readTree(response.body()).get("access_token").asText();
	}

	private ConcurrentHashMap<String, String> convertAccessTokenToOAuthInformation(String accessToken) throws
		IOException, InterruptedException {
		// User정보 받는 요청 보내기 (GET + Header)
		ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder(URI.create("https://kapi.kakao.com/v2/user/me"))
			.GET()
			.setHeader("Authorization", "Bearer " + accessToken)
			.setHeader("Content-Type", "application/json;charset=utf-8")
			.build();

		var result = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
		var tree = new ObjectMapper().readTree(result.body());
		var oauthUserProfile = tree.get("kakao_account").get("profile");

		concurrentHashMap.put("id", tree.get("id").asText());
		concurrentHashMap.put("nickname", oauthUserProfile.get("nickname").asText());
		concurrentHashMap.put("profile_image_url", oauthUserProfile.get("profile_image_url").asText());
		return concurrentHashMap;
	}

	private String generateToken(User user) {
		return jwt().sign(Jwt.Claims.of(user.getId(), "ROLE_USER"));
	}

	private Jwt jwt() {
		return new Jwt(
			jwtConfigure.getIssuer(),
			jwtConfigure.getClientSecret(),
			jwtConfigure.getExpirySeconds()
		);
	}
}
