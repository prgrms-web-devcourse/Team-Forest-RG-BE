package com.prgrms.rg.domain.ridingpost.application;

import java.util.List;

import com.prgrms.rg.domain.ridingpost.application.command.RidingPostCommentCreateCommand;
import com.prgrms.rg.domain.ridingpost.application.information.RidingPostCommentInfo;

public interface RidingPostCommentService {
	long createComment(RidingPostCommentCreateCommand command);

	List<RidingPostCommentInfo> getCommentsByPostId(long ridingPostId);

	void updateComment(long userId, long commentId, String contents);

	void removeComment(long userId, long commentId);
}
