package com.prgrms.rg.infrastructure.oauth;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class KakaoOauth implements OAuthManager {

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

	public Map<String, String> convertAuthorizationCodeToInfo(String authorizationCode) throws
		IOException,
		InterruptedException {
		return convertAccessTokenToOAuthInformation(convertAuthorizationCodeToAccessToken(authorizationCode));
	}

	private String convertAuthorizationCodeToAccessToken(String authorizationCode) throws
		IOException,
		InterruptedException {
		// access 토큰 받는 요청 보내기 (POST)
		var urlEncodedBody = new String(new UrlEncodedFormEntity(
			List.of(
				new BasicNameValuePair("client_id", clientId),
				new BasicNameValuePair("client_secret", clientSecret),
				new BasicNameValuePair("code", authorizationCode),
				new BasicNameValuePair("grant_type", grantType),
				new BasicNameValuePair("redirect_url", redirectUrl)
			)
		).getContent().readAllBytes());

		HttpRequest request = HttpRequest
			.newBuilder(URI.create(tokenUrl))
			.POST(HttpRequest.BodyPublishers.ofString(urlEncodedBody))
			.header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
			.build();

		HttpClient client = HttpClient.newHttpClient();
		var response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		return new ObjectMapper().readTree(response.body()).get("access_token").asText();
	}

	private Map<String, String> convertAccessTokenToOAuthInformation(String accessToken) throws
		IOException, InterruptedException {
		// User정보 받는 요청 보내기 (GET + Header)
		Map<String, String> infoResult = new HashMap<>();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder(URI.create(userInfoUrl))
			.GET()
			.setHeader("Authorization", "Bearer " + accessToken)
			.setHeader("Content-Type", "application/json;charset=utf-8")
			.build();

		var result = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
		var tree = new ObjectMapper().readTree(result.body());
		var oauthUserProfile = tree.get("kakao_account").get("profile");

		infoResult.put("id", tree.get("id").asText());
		infoResult.put("nickname", oauthUserProfile.get("nickname").asText());
		infoResult.put("profile_image_url", oauthUserProfile.get("profile_image_url").asText());
		return infoResult;
	}
}
