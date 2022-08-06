package com.prgrms.rg.domain.user.application.impl;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.Transactional;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.user.application.UserService;
import com.prgrms.rg.domain.user.model.Manner;
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
	private final JwtTokenProvider jwtTokenProvider;

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String clientId;
	@Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
	private String clientSecret;
	@Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
	private String grantType;
	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String redirectUrl;
	@Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
	private String tokenUrl;
	@Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
	private String userInfoUrl;

	// private final String provider = "kakao";

	@Override
	@Transactional
	public Optional<User> findUserById(Long id) {
		checkArgument(isNotEmpty(id), "id must be provided.");
		return userRepository.findById(id);
	}

	@Override
	@Transactional
	public OAuthLoginResult joinOAuth(String authorizationCode, String fromUrl) throws Exception {
		checkArgument(authorizationCode != null, "authorizationCode must be provided");
		String accessToken = convertAuthorizationCodeToAccessToken(authorizationCode);
		ConcurrentHashMap<String, String> oauthInformation = convertAccessTokenToOAuthInformation(accessToken);
		String providerId = oauthInformation.get("id");
		String provider = "kakao";
		return userRepository.findByProviderAndProviderId(provider, providerId)
			.map(user -> {
				log.warn("Already exists: {} for (provider: {}, providerId: {})", user, provider,
					providerId);
				String token = generateToken(user);
				return OAuthLoginResult.of(token, false, fromUrl);
			})
			.orElseGet(() -> {
				@SuppressWarnings("unchecked")
				String nickname = oauthInformation.get("nickname");
				String profileImage = oauthInformation.get("profile_image");
				User user = userRepository.save(User.builder()
					.nickname(new Nickname(nickname))
					.profileImages(profileImage)
					.providerId(providerId)
					.provider(provider)
					.manner(Manner.create())
					.build());
				String token = generateToken(user);
				return OAuthLoginResult.of(token, true, fromUrl);
			});
	}

	private String convertAuthorizationCodeToAccessToken(String authorizationCode) throws
		IOException,
		InterruptedException {
		var urlEncodedBody = new String(new UrlEncodedFormEntity(
			List.of(
				new BasicNameValuePair("client_id", clientId),
				new BasicNameValuePair("client_secret", clientSecret),
				new BasicNameValuePair("code", authorizationCode),
				new BasicNameValuePair("grant_type", grantType),
				new BasicNameValuePair("redirect_url", redirectUrl)
			)
		).getContent().readAllBytes());
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest
			.newBuilder(URI.create(tokenUrl))
			.POST(HttpRequest.BodyPublishers.ofString(urlEncodedBody))
			.header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
			.build();
		var response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		return new ObjectMapper().readTree(response.body()).get("access_token").asText();
	}

	private ConcurrentHashMap<String, String> convertAccessTokenToOAuthInformation(String accessToken) throws
		IOException, InterruptedException {
		ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest
			.newBuilder(URI.create(userInfoUrl))
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
		return jwtTokenProvider.createToken("ROLE_USER", user.getId());
	}
}
