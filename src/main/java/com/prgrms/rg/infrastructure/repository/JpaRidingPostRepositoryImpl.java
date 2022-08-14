package com.prgrms.rg.infrastructure.repository;

import static com.prgrms.rg.domain.ridingpost.model.QRidingPost.*;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingStatus;
import com.prgrms.rg.infrastructure.repository.custom.JpaRidingPostRepositoryCustom;

@Repository
public class JpaRidingPostRepositoryImpl extends QuerydslRepositorySupport implements JpaRidingPostRepositoryCustom {

	public JpaRidingPostRepositoryImpl() {
		super(RidingPost.class);
	}

	@Override
	public void updateRidingPostListToClosed(List<RidingPost> postList) {
		update(ridingPost)
			.set(ridingPost.ridingParticipantSection.status, RidingStatus.CLOSED)
			.where(ridingPost.in(postList)).execute();
	}
}
