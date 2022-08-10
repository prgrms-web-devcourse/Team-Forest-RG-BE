package com.prgrms.rg.domain.ridingpost.application.impl;

import org.springframework.stereotype.Service;

import com.prgrms.rg.domain.common.RelatedEntityNotFoundException;
import com.prgrms.rg.domain.ridingpost.application.RidingPostCommentService;
import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingPostCommentCreateCommand;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostComment;
import com.prgrms.rg.domain.ridingpost.model.RidingPostCommentRepository;
import com.prgrms.rg.domain.user.application.UserReadService;
import com.prgrms.rg.domain.user.model.User;

@Service
public class RidingPostCommentServiceImpl implements RidingPostCommentService {

	private final UserReadService userReadService;

	private final RidingPostReadService ridingPostReadService;

	private final RidingPostCommentRepository ridingPostCommentRepository;

	public RidingPostCommentServiceImpl(UserReadService userReadService, RidingPostReadService ridingPostReadService,
		RidingPostCommentRepository ridingPostCommentRepository) {
		this.userReadService = userReadService;
		this.ridingPostReadService = ridingPostReadService;
		this.ridingPostCommentRepository = ridingPostCommentRepository;
	}

	@Override
	public long createComment(RidingPostCommentCreateCommand command) {
		User author;
		RidingPost post;
		try {
			author = userReadService.getUserEntityById(command.getAuthorId());
			post = ridingPostReadService.getRidingPostById(command.getPostId());
		} catch (RuntimeException exception) {
			throw new RelatedEntityNotFoundException(exception);
		}

		var comment = RidingPostComment.of(author, post, command.getContent());
		return ridingPostCommentRepository.save(comment).getId();
	}
}
