package com.prgrms.rg.domain.ridingpost.model;

public interface RidingPostCommentRepository {
	RidingPostComment findById(long commentId);

	RidingPostComment save(RidingPostComment ridingPostComment);
}
