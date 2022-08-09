package com.prgrms.rg.domain.user.application.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.user.application.UserAuthenticationService;
import com.prgrms.rg.infrastructure.oauth.OAuthManager;
import com.prgrms.rg.web.user.results.OAuthLoginResult;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserAuthenticationServiceImplTest {
	@MockBean
	OAuthManager communicator;

	@Autowired
	private UserAuthenticationService userService;

	private static ConcurrentMap<String, String> concurrentMap;

	@BeforeAll
	static void 초기값_셋팅() {
		concurrentMap = new ConcurrentHashMap<>();
		concurrentMap.put("id", "testid");
		concurrentMap.put("nickname", "nickname");
		concurrentMap.put("profile_image", "profileImage");
	}

	@Test
	void joinOAuth_성공_테스트() throws Exception {
		//given
		String authorizationCode = "test Authorization Code";
		given(communicator.convertAuthorizationCodeToInfo(authorizationCode)).willReturn(concurrentMap);
		//when
		OAuthLoginResult oAuthLoginResult = userService.joinOAuth(authorizationCode);
		//then
		assertThat(oAuthLoginResult.getAccessToken()).isNotNull();
	}

	@Test
	void joinOAuth_AuthorizationCode빈문자열_실패_테스트() throws Exception {
		//given
		String authorizationCode = "";
		given(communicator.convertAuthorizationCodeToInfo(authorizationCode)).willReturn(concurrentMap);
		//when //then
		assertThatThrownBy(() -> {
			userService.joinOAuth(authorizationCode);
		}).isInstanceOf(IllegalArgumentException.class);
	}
}