package com.prgrms.rg.domain.user.application.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.user.application.UserAuthenticationService;
import com.prgrms.rg.domain.user.application.command.UserRegisterCommand;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;
import com.prgrms.rg.infrastructure.oauth.OAuthManager;
import com.prgrms.rg.testutil.TestEntityDataFactory;
import com.prgrms.rg.web.user.requests.UserRegisterRequest;
import com.prgrms.rg.web.user.results.OAuthLoginResult;
import com.prgrms.rg.web.user.results.UserRegisterResult;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserAuthenticationServiceImplTest {
	@MockBean
	OAuthManager communicator;

	@Autowired
	private UserAuthenticationService userService;

	@Autowired
	private UserRepository userRepository;

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
	void joinOAuth_AuthorizationCode빈문자열_실패_테스트() throws IOException, InterruptedException {
		//given
		String authorizationCode = "";
		given(communicator.convertAuthorizationCodeToInfo(authorizationCode)).willReturn(concurrentMap);
		//when //then
		assertThatThrownBy(() -> {
			userService.joinOAuth(authorizationCode);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void updateUserByRegistration_성공_테스트() {
		//given
		User user = TestEntityDataFactory.createUser();
		//when
		User savedUser = userRepository.save(user);
		UserRegisterCommand testCommand = createTestRegisterCommand(savedUser.getId());
		UserRegisterResult userRegisterResult = userService.updateUserByRegistration(testCommand);
		User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();
		//then
		assertThat(userRegisterResult.isRegistered()).isTrue();
		assertThat(updatedUser.getNickname()).isEqualTo(testCommand.getNickName());
		assertThat(updatedUser.getAddressCode().getCode()).isEqualTo(23);
		assertThat(updatedUser.getRiderInformation().getBicycles()).hasSize(2);
		assertThat(updatedUser.getRiderInformation().getRidingYears()).isEqualTo(1996);
		assertThat(updatedUser.getPhoneNumber()).isEqualTo("010-1234-5678");
		assertThat(updatedUser.getRiderInformation().getLevel()).isEqualTo(RidingLevel.BEGINNER);


	}

	private UserRegisterCommand createTestRegisterCommand(Long userId) {
		List<String> bicycles = new ArrayList<>();
		bicycles.add("MTB");
		bicycles.add("Road");
		return UserRegisterCommand.of(new UserRegisterRequest(1996,23,bicycles,"하", "010-1234-5678","김훈기"),userId);
	}
}