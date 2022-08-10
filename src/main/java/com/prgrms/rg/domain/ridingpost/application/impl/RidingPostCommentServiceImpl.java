package com.prgrms.rg.domain.ridingpost.application.impl;

import org.springframework.stereotype.Service;

import com.prgrms.rg.domain.ridingpost.application.RidingPostCommentService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingPostCommentCreateCommand;

@Service
public class RidingPostCommentServiceImpl implements RidingPostCommentService {
	@Override
	public long createComment(RidingPostCommentCreateCommand ridingPostCommentCreateCommand) {
		return 0;
	}
}
