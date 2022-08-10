package com.prgrms.rg.domain.ridingpost.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.ridingpost.application.RidingPostService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingCreateCommand;
import com.prgrms.rg.domain.ridingpost.model.RidingCreateManagement;
import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;
import com.prgrms.rg.domain.user.application.UserReadService;
import com.prgrms.rg.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RidingPostServiceImpl implements RidingPostService {

	private final UserReadService userReadService;
	private final RidingPostRepository ridingPostRepository;
	private final RidingCreateManagement createManagement;

	@Transactional
	public Long createRidingPost(Long userId, RidingCreateCommand command) {
		User user = userReadService.getUserEntityById(userId);

		//post, subsection 저장
		var savedPost = ridingPostRepository.save(createManagement.createRidingPost(user, command));

		//saved의 id return
		return savedPost.getId();
	}
}
