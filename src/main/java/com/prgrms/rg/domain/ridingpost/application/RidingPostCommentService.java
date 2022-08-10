package com.prgrms.rg.domain.ridingpost.application;

import com.prgrms.rg.domain.ridingpost.application.command.RidingPostCommentCreateCommand;

public interface RidingPostCommentService {
	long createComment(RidingPostCommentCreateCommand command);
}
