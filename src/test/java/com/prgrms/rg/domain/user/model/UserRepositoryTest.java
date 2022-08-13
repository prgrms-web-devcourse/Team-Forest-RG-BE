package com.prgrms.rg.domain.user.model;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	private final String provider = "kakao";
	private final String providerId = "1234567";

	@BeforeEach
	void save_정상_테스트() {
		//given
		String profileImage = "profileImage";
		String nickname = "nickname";
		User user = userRepository.save(User.builder()
			.nickname(new Nickname(nickname))
			.providerId(providerId)
			.provider(provider)
			.manner(Manner.create())
			.build());
		//when
		User savedUser = userRepository.save(user);
		// then
		assertThat(savedUser.getProviderId()).isEqualTo(providerId);
	}

	@Test
	void findByProviderAndProviderId_정상_테스트() {
		//given
		//전역변수
		//when
		Optional<User> selectedUser = userRepository.findByProviderAndProviderId(provider, providerId);
		//then
		assertThat(selectedUser).isPresent();
		assertThat(selectedUser.get().getProviderId()).isEqualTo(providerId);
	}

	@Test
	void findByProviderAndProviderId_잘못된provider_비정상_테스트() {
		//given
		String testProvider = "google";
		//when
		Optional<User> selectedUser = userRepository.findByProviderAndProviderId(testProvider, providerId);
		//then
		assertThat(selectedUser).isEmpty();
	}

	@Test
	void findByProviderAndProviderId_잘못된providerId_비정상_테스트() {
		//given
		String testProviderId = "1234568";
		//when
		Optional<User> selectedUser = userRepository.findByProviderAndProviderId(provider, testProviderId);
		//then
		assertThat(selectedUser).isEmpty();
	}
}