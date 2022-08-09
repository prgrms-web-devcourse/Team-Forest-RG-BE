package com.prgrms.rg.domain.ridingpost.model;

import java.util.Optional;

import com.prgrms.rg.domain.user.model.User;

public interface RidingPostRepository {
	Optional<RidingPost> findById(Long postId);

	Optional<RidingPost> findByLeaderAndId(User leader, Long postId);
	RidingPost save(RidingPost post);
}
