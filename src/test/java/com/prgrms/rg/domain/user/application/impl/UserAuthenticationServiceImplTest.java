package com.prgrms.rg.domain.user.application.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.common.model.metadata.BicycleRepository;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.ridingpost.model.AddressCodeRepository;
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
@Transactional
class UserAuthenticationServiceImplTest {
	@MockBean
	OAuthManager communicator;

	@Autowired
	private UserAuthenticationService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BicycleRepository bicycleRepository;

	@Autowired
	private AddressCodeRepository addressCodeRepository;

	private static ConcurrentMap<String, String> concurrentMap;


	@PersistenceContext
	EntityManager em;

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
	@Sql(scripts = {"classpath:address_code.sql", "classpath:bicycle.sql"})
	void updateUserByRegistration_성공_테스트() {
		//given
		User user = TestEntityDataFactory.createUser();
		User savedUser = userRepository.save(user);
		UserRegisterCommand testCommand = createTestRegisterCommand(savedUser.getId());
		em.flush();
		em.clear();
		//when
		UserRegisterResult userRegisterResult = userService.updateUserByRegistration(testCommand);
		//then
		assertThat(userRepository.findById(savedUser.getId()).isPresent()).isTrue();
		User updatedUser = userRepository.findById(savedUser.getId()).get();
		assertThat(userRegisterResult.isRegistered()).isTrue();
		assertThat(updatedUser.getNickname()).isEqualTo(testCommand.getNickName());
		assertThat(updatedUser.getAddressCode().getCode()).isEqualTo(11010);
		assertThat(updatedUser.getRiderInformation().getBicycles()).hasSize(1);
		assertThat(updatedUser.getRiderInformation().getRidingYears()).isEqualTo(1996);
		assertThat(updatedUser.getPhoneNumber()).isEqualTo("010-1234-5678");
		assertThat(updatedUser.getRiderInformation().getLevel()).isEqualTo(RidingLevel.BEGINNER);
	}

	private UserRegisterCommand createTestRegisterCommand(Long userId) {
		List<String> bicycles = new ArrayList<>();
		bicycles.add("MTB");
		return UserRegisterCommand.of(new UserRegisterRequest(1996,11010,bicycles,"하", "010-1234-5678","김훈기"),userId);
	}
}