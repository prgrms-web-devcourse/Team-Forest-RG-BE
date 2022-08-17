package com.prgrms.rg.infrastructure.repository.querydslconditions;

import java.time.LocalDateTime;
import java.util.List;

import com.prgrms.rg.domain.ridingpost.model.QRidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.user.model.User;
import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.dsl.BooleanExpression;

public class RidingPostConditional {

	@QueryDelegate(RidingPost.class)
	public static BooleanExpression userConditionOf(QRidingPost post, User user, RidingPostUserSearchType type) {
		switch (type) {
			case LEADER: return isHostedBy(post, user);
			case PARTICIPATED:return isParticipatedBy(post, user).and(isEnded(post));
			case WILL_PARTICIPATE: return isParticipatedBy(post, user).and(isNotEnded(post));
			case WILL_EVALUATE: return isEnded(post);
			default: throw new IllegalArgumentException("부적절한 검색 조건 대입함");
		}
	}

	private static BooleanExpression isHostedBy(QRidingPost post, User leader) {
		return post.leader.eq(leader);
	}

	private static BooleanExpression isParticipatedBy(QRidingPost post, User participant) {
		return post.ridingParticipantSection.participants.any().user.eq(participant);
	}

	private static BooleanExpression isEnded(QRidingPost post) {
		return post.ridingMainSection.ridingDate.before(LocalDateTime.now());
	}

	private static BooleanExpression isNotEnded(QRidingPost post) {
		return post.ridingMainSection.ridingDate.after(LocalDateTime.now());
	}

	@QueryDelegate(RidingPost.class)
	public static BooleanExpression willEvaluated(QRidingPost post, List<Long> postIds) {
		return post.id.in(postIds);
	}
}
