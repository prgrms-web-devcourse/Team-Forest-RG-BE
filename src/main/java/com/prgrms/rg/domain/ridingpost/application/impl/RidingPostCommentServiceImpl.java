package com.prgrms.rg.domain.ridingpost.application.impl;

import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.common.RelatedEntityNotFoundException;
import com.prgrms.rg.domain.ridingpost.application.RidingPostCommentService;
import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingPostCommentCreateCommand;
import com.prgrms.rg.domain.ridingpost.application.information.RidingPostCommentInfo;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostComment;
import com.prgrms.rg.domain.ridingpost.model.RidingPostCommentRepository;
import com.prgrms.rg.domain.user.application.UserReadService;
import com.prgrms.rg.domain.user.model.User;

@Service
@Transactional(readOnly = true)
public class RidingPostCommentServiceImpl implements RidingPostCommentService {

	private final UserReadService userReadService;

	private final RidingPostReadService ridingPostReadService;

	private final RidingPostCommentRepository ridingPostCommentRepository;

	public RidingPostCommentServiceImpl(
		UserReadService userReadService,
		RidingPostReadService ridingPostReadService,
		RidingPostCommentRepository ridingPostCommentRepository) {
		this.userReadService = userReadService;
		this.ridingPostReadService = ridingPostReadService;
		this.ridingPostCommentRepository = ridingPostCommentRepository;
	}

	/*
	parentId가 있으면 대댓글
	parentId가 없으면 댓글
	 */
	@Override
	@Transactional
	public long createComment(RidingPostCommentCreateCommand command) {
		User author;
		RidingPost post;
		Long parentId = command.getParentCommentId();

		try {
			author = userReadService.getUserEntityById(command.getAuthorId());
			post = ridingPostReadService.loadRidingPostById(command.getPostId());
		} catch (NoSuchElementException exception) {
			throw new RelatedEntityNotFoundException(exception);
		}

		// 부모 comment가 있을 경우 자식 comment는 post와 연관 관계를 갖지 않는다.
		// 추후 postid 기반으로 탐색할 때 nested된 comment들을 탐색하는 것을 막기 위함
		if (Objects.nonNull(parentId) && parentId > 0) {
			var parentComment = ridingPostCommentRepository.findById(parentId);
			var comment = RidingPostComment.createChildComment(author, parentComment, command.getContent());
			comment = ridingPostCommentRepository.save(comment);
			ridingPostCommentRepository.save(parentComment);
			return comment.getId();
		}
		var comment = RidingPostComment.createRootComment(author, post, command.getContent());
		return ridingPostCommentRepository.save(comment).getId();
	}

	@Override
	public List<RidingPostCommentInfo> getCommentsByPostId(long ridingPostId) {
		RidingPost post;
		try {
			post = ridingPostReadService.loadRidingPostById(ridingPostId);
		} catch (NoSuchElementException exception) {
			throw new RelatedEntityNotFoundException(exception);
		}

		var comments = ridingPostCommentRepository.findAllByRidingPost(post);

		return comments.stream()
			.map(RidingPostCommentInfo::from)
			.sorted(Comparator.comparing(RidingPostCommentInfo::getCreatedAt))
			.collect(Collectors.toUnmodifiableList());

	}

}
