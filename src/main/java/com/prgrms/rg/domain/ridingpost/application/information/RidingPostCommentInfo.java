package com.prgrms.rg.domain.ridingpost.application.information;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.prgrms.rg.domain.ridingpost.model.RidingPostComment;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RidingPostCommentInfo {
	private final long commentId;
	private final long parentCommentId;
	private final long authorId;
	private final String authorName;
	private final String authorImageUrl;
	private final String contents;
	private final LocalDateTime createdAt;
	private final List<RidingPostCommentInfo> childComments;

	public static RidingPostCommentInfo from(RidingPostComment comment) {
		return builder()
			.authorId(comment.getAuthor().getId())
			.parentCommentId(comment.getParentComment() == null ? 0L : comment.getParentComment().getId())
			.childComments(comment.getChildComments()
				.stream()
				.map(RidingPostCommentInfo::from)
				.sorted(Comparator.comparing(RidingPostCommentInfo::getCreatedAt))
				.collect(Collectors.toUnmodifiableList()))
			.commentId(comment.getId())
			.authorName(comment.getAuthor().getNickname())
			.contents(comment.getContents())
			.createdAt(comment.getCreatedAt())
			.authorImageUrl(comment.getAuthor().getImage().getFileUrl())
			.build();
	}
}
