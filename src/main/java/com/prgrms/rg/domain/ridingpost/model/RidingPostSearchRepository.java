package com.prgrms.rg.domain.ridingpost.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.prgrms.rg.domain.ridingpost.application.information.RidingPostBriefInfo;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.infrastructure.repository.querydslconditions.RidingPostUserSearchType;

public interface RidingPostSearchRepository {
	Slice<RidingPostInfo> searchRidingPostSlice(RidingSearchCondition condition, Pageable pageable);

	List<RidingPostBriefInfo> searchRidingPostByUser(User user, RidingPostUserSearchType searchType);

	List<RidingPost> searchRidingPostInProgress();

}