package com.prgrms.rg.domain.ridingpost.model;

import java.util.List;
import java.util.Optional;

public interface RidingPostRepository {
	Optional<RidingPost> findById(Long postId);

	RidingPost save(RidingPost post);

	void updateRidingPostListToClosed(List<RidingPost> postList);

	void deleteById(Long postId);
}
