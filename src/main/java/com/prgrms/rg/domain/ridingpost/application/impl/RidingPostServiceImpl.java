package com.prgrms.rg.domain.ridingpost.application.impl;

import static com.google.common.base.Preconditions.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingSaveManagement;
import com.prgrms.rg.domain.ridingpost.application.RidingPostService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSaveCommand;
import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingPostNotFoundException;
import com.prgrms.rg.domain.ridingpost.model.exception.UnAuthorizedException;
import com.prgrms.rg.domain.user.application.UserReadService;
import com.prgrms.rg.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RidingPostServiceImpl implements RidingPostService {

	private final UserReadService userReadService;
	private final RidingPostRepository ridingPostRepository;
	private final RidingSaveManagement saveManagement;

	@Transactional
	public Long createRidingPost(Long userId, RidingSaveCommand command) {
		User user = userReadService.getUserEntityById(userId);

		//post, subsection 저장
		var savedPost = ridingPostRepository.save(saveManagement.createRidingPost(user, command));

		//saved의 id return
		return savedPost.getId();
	}

	@Override
	@Transactional
	public Long updateRidingPost(Long leaderId, Long postId, RidingSaveCommand command) {

		var post = checkPostOperationCommand(leaderId, postId);
		saveManagement.updateRidingPost(post.getLeader(), post, command);

		return post.getId();
	}

	@Override
	@Transactional(readOnly = true)
	public void deleteRidingPost(Long leaderId, Long postId) {
		checkPostOperationCommand(leaderId, postId);

		ridingPostRepository.deleteById(postId);
	}

	private RidingPost checkPostOperationCommand(Long leaderId, Long postId) {
		var post = ridingPostRepository.findById(postId).orElseThrow(() -> new RidingPostNotFoundException(postId));
		var leader = post.getLeader();
		checkArgument(leader.getId().equals(leaderId), new UnAuthorizedException(leaderId));
		return post;
	}

}
