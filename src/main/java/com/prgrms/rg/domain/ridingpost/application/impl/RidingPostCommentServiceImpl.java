package com.prgrms.rg.domain.ridingpost.application.impl;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
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
import com.prgrms.rg.domain.ridingpost.model.exception.UnAuthorizedException;
import com.prgrms.rg.domain.user.application.UserReadService;
import com.prgrms.rg.domain.user.model.User;

@Service
@Transactional
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

	@Override
	@Transactional
	public long createComment(RidingPostCommentCreateCommand command) {
		User author;
		RidingPost post;

		try {
			author = userReadService.getUserEntityById(command.getAuthorId());
			post = ridingPostReadService.loadRidingPostById(command.getPostId());
		} catch (NoSuchElementException exception) {
			throw new RelatedEntityNotFoundException(exception);
		}

		Long parentId = command.getParentCommentId();
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
	@Transactional(readOnly = true)
	public List<RidingPostCommentInfo> getCommentsByPostId(long ridingPostId) {
		RidingPost post;
		try {
			post = ridingPostReadService.loadRidingPostById(ridingPostId);
		} catch (NoSuchElementException exception) {
			throw new RelatedEntityNotFoundException(exception);
		}

		var comments = ridingPostCommentRepository.findAllByRidingPostAndParentCommentIsNull(post);

		return comments.stream()
			.map(RidingPostCommentInfo::from)
			.sorted(Comparator.comparing(RidingPostCommentInfo::getCreatedAt))
			.collect(Collectors.toUnmodifiableList());

	}

	@Override
	@Transactional
	public void updateComment(long userId, long commentId, String contents) {
		var comment = fetchAuthorizedComment(userId, commentId);
		comment.changeContents(contents);

	}

	@Override
	public void removeComment(long userId, long commentId) {
		var comment = fetchAuthorizedComment(userId, commentId);
		ridingPostCommentRepository.delete(comment);
	}

	private RidingPostComment fetchAuthorizedComment(long userId, long commentId) {
		User requestingUser;
		RidingPostComment comment;
		try {
			requestingUser = userReadService.getUserEntityById(userId);
			comment = ridingPostCommentRepository.findById(commentId);
			if (!Objects.equals(requestingUser, comment.getAuthor())) {
				throw new UnAuthorizedException(userId);
			}
		} catch (NoSuchElementException exception) {
			throw new RelatedEntityNotFoundException(exception);
		}
		return comment;
	}

}
