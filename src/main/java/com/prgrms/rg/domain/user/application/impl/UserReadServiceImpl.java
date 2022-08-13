package com.prgrms.rg.domain.user.application.impl;

import static com.prgrms.rg.infrastructure.repository.querydslconditions.RidingPostUserSearchType.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.ridingpost.application.information.RidingPostBriefInfo;
import com.prgrms.rg.domain.ridingpost.model.RidingPostSearchRepository;
import com.prgrms.rg.domain.user.application.exception.NoSuchUserException;
import com.prgrms.rg.domain.user.application.UserReadService;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;
import com.prgrms.rg.domain.user.model.information.ParticipatedRidingInfo;
import com.prgrms.rg.domain.user.model.information.UserProfilePageInfo;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserReadServiceImpl implements UserReadService {

	private final UserRepository userRepository;
	private final RidingPostSearchRepository postSearchRepository;

	@Override
	public User getUserEntityById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new NoSuchUserException(userId));
	}

	@Override
	public UserProfilePageInfo getUserProfilePageInfo(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchUserException(userId));
		List<RidingPostBriefInfo> leading = postSearchRepository.searchRidingPostByUser(user, LEADER);
		List<RidingPostBriefInfo> finished = postSearchRepository.searchRidingPostByUser(user, PARTICIPATED);
		List<RidingPostBriefInfo> scheduled = postSearchRepository.searchRidingPostByUser(user, WILL_PARTICIPATE);
		List<RidingPostBriefInfo> canEvaluate = postSearchRepository.searchRidingPostByUser(user, WILL_EVALUATE);

		ParticipatedRidingInfo participatedRidingInfo = ParticipatedRidingInfo.from(leading, finished, scheduled, canEvaluate);

		return UserProfilePageInfo.from(user, participatedRidingInfo);
	}
}
