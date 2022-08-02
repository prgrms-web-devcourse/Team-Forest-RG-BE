package com.prgrms.rg.domain.user.model;

import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.ridingpost.model.dummy.DummyUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserFinder {
	private final DummyUserRepository userRepository;

	public User find(Long userId) {
		return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
	}

}
