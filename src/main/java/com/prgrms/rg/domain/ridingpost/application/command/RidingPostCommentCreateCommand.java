package com.prgrms.rg.domain.ridingpost.application.command;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RidingPostCommentCreateCommand {
	private final long authorId;
	private final long postId;
	private final String content;

	public static RidingPostCommentCreateCommand of(long authorId, long postId, String content) {
		return new RidingPostCommentCreateCommand(authorId, postId, content);
	}
}
