package com.prgrms.rg.domain.user.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.user.application.NoSuchUserException;
import com.prgrms.rg.domain.user.application.UserReadService;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;
import com.prgrms.rg.domain.user.model.information.UserProfilePageInfo;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserReadServiceImpl implements UserReadService {

	private final UserRepository userRepository;

	@Override
	public UserProfilePageInfo getUserProfilePageInfo(Long userId) {
		return UserProfilePageInfo.of(userRepository.findById(userId).orElseThrow(() -> new NoSuchUserException(userId)));
	}
}
