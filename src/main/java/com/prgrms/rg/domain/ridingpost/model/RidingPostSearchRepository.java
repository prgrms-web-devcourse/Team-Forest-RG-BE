package com.prgrms.rg.domain.ridingpost.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.infrastructure.repository.projections.querydsl.RidingPostBriefInfoQueryDslProjection;
import com.prgrms.rg.infrastructure.repository.querydslconditions.RidingPostUserSearchType;

public interface RidingPostSearchRepository {
	Slice<RidingPostInfo> searchRidingPostSlice(RidingSearchCondition condition, Pageable pageable);

	List<RidingPostBriefInfoQueryDslProjection> searchRidingPostByUser(User user, RidingPostUserSearchType searchType);

	List<RidingPostBriefInfoQueryDslProjection> searchEvaluabledRidingPostByUser(User user);
}