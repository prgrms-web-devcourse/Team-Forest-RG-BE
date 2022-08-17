package com.prgrms.rg.infrastructure.repository.custom;

import java.util.List;

import com.prgrms.rg.domain.ridingpost.model.RidingPost;

public interface JpaRidingPostRepositoryCustom {

	void updateRidingPostListToClosed(List<RidingPost> postList);
}
