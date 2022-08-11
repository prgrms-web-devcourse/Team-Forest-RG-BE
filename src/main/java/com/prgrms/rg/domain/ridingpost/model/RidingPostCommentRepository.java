package com.prgrms.rg.domain.ridingpost.model;

import java.util.List;

public interface RidingPostCommentRepository {
	RidingPostComment findById(long commentId);

	RidingPostComment save(RidingPostComment ridingPostComment);

	List<RidingPostComment> findAllByRidingPost(RidingPost ridingPost);

	void delete(RidingPostComment ridingPostComment);
}
