package com.prgrms.rg.domain.user.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.user.application.NoSuchUserException;
import com.prgrms.rg.domain.user.application.UserService;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public User getUserInformation(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new NoSuchUserException(userId));
	}
}
