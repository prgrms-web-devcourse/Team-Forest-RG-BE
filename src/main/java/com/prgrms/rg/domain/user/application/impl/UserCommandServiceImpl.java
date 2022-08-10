package com.prgrms.rg.domain.user.application.impl;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.prgrms.rg.domain.common.model.metadata.Bicycle;
import com.prgrms.rg.domain.common.model.metadata.BicycleRepository;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.user.application.UserCommandService;
import com.prgrms.rg.domain.user.application.command.UserUpdateCommand;
import com.prgrms.rg.domain.user.application.exception.DuplicateNicknameException;
import com.prgrms.rg.domain.user.application.exception.NoSuchUserException;
import com.prgrms.rg.domain.user.model.Introduction;
import com.prgrms.rg.domain.user.model.Nickname;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserBicycle;
import com.prgrms.rg.domain.user.model.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

	private final UserRepository userRepository;
	private final BicycleRepository bicycleRepository;

	@Override
	public Long edit(UserUpdateCommand command) {
		User user = userRepository.findById(command.getId())
			.orElseThrow((() -> new NoSuchUserException(command.getId())));

		changeNickname(command.getNickname(), user);
		changeRiderProfile(command, user);
		user.changeIntroduction(new Introduction(command.getIntroduction()));

		return user.getId();
	}

	private void changeNickname(String nickname, User user) {
		Nickname nicknameToChange = new Nickname(nickname);
		if (nickname.equals(user.getNickname())) {
			return;
		}
		if (userRepository.existsUserByNickname(nicknameToChange)) {
			throw new DuplicateNicknameException(nicknameToChange);
		}
		user.changeNickname(nicknameToChange);
	}

	private void changeRiderProfile(UserUpdateCommand command, User user) {
		String[] inputBicycles = command.getBicycles();
		Set<UserBicycle> bicyclesToApply = new HashSet<>();

		for (String inputBicycle : inputBicycles) {
			Bicycle bicycle = bicycleRepository.findByName(inputBicycle).orElseThrow();
			bicyclesToApply.add(new UserBicycle(user, bicycle));
		}

		user.changeRiderProfile(command.getRidingYears(), RidingLevel.of(command.getRidingLevel()), bicyclesToApply);
	}
}