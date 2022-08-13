package com.prgrms.rg.infrastructure.repository;

import static com.prgrms.rg.domain.common.model.metadata.QBicycle.*;
import static com.prgrms.rg.domain.ridingpost.model.QRidingConditionBicycle.*;
import static com.prgrms.rg.domain.ridingpost.model.QRidingParticipant.*;
import static com.prgrms.rg.domain.ridingpost.model.QRidingPost.*;
import static com.prgrms.rg.infrastructure.repository.querydslconditions.RidingPostUserSearchType.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.prgrms.rg.domain.common.model.metadata.Bicycle;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostInfo;
import com.prgrms.rg.domain.ridingpost.model.RidingPostSearchRepository;
import com.prgrms.rg.domain.ridingpost.model.RidingSearchCondition;
import com.prgrms.rg.domain.ridingpost.model.RidingStatus;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.infrastructure.repository.projections.querydsl.QRidingBicyclesInfoQueryDslProjection;
import com.prgrms.rg.infrastructure.repository.projections.querydsl.QRidingPostBriefInfoQueryDslProjection;
import com.prgrms.rg.infrastructure.repository.projections.querydsl.RidingBicyclesInfoQueryDslProjection;
import com.prgrms.rg.infrastructure.repository.projections.querydsl.RidingPostBriefInfoQueryDslProjection;
import com.prgrms.rg.infrastructure.repository.querydslconditions.RidingPostUserSearchType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class QuerydslRidingPostSearchRepository extends QuerydslRepositorySupport
	implements RidingPostSearchRepository {

	private static final int MAXIMUM_USER_SEARCH_RESULT = 20;

	public QuerydslRidingPostSearchRepository() {
		super(RidingPost.class);
	}

	//TODO 최적화 꼭 필요. applySorting 관련 이슈 확인할것
	@Override
	public Slice<RidingPostInfo> searchRidingPostSlice(RidingSearchCondition condition, Pageable pageable) {
		JPQLQuery<RidingPost> query = from(ridingPost)
			.leftJoin(ridingPost.leader).fetchJoin() //m:1
			.leftJoin(ridingPost.thumbnail).fetchJoin() // 1:1
			.leftJoin(ridingConditionBicycle).on(ridingConditionBicycle.post.id.eq(ridingPost.id)).fetchJoin()
			.leftJoin(bicycle).on(bicycle.id.eq(ridingConditionBicycle.id)).fetchJoin()
			.where(ridingLevelEq(condition.getRidingLevel()),
				ridingStatusEq(condition.getRidingStatus()),
				zoneEq(condition.getAddressCode()),
				bicycleEq(condition.getBicycleCode())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1L)
			.select(ridingPost).distinct();

		Objects.requireNonNull(getQuerydsl()).applySorting(pageable.getSort(), query);

		List<RidingPost> result = query.fetch();
		return convertResultAsSlice(result, pageable);
	}

	private Slice<RidingPostInfo> convertResultAsSlice(List<RidingPost> result, Pageable pageable) {
		boolean hasNext = false;
		if (result.size() > pageable.getPageSize()) {
			hasNext = true;
			result.remove(pageable.getPageSize());
		}

		List<RidingPostInfo> ridingInfos = result.stream()
			.map(RidingPostInfo::from)
			.collect(Collectors.toList());

		return new SliceImpl<>(ridingInfos, pageable, hasNext);
	}

	private BooleanExpression ridingLevelEq(String ridingLevel) {
		return ridingLevel != null ? ridingPost.ridingConditionSection.level.eq(RidingLevel.of(ridingLevel)) : null;
	}

	private BooleanExpression ridingStatusEq(RidingStatus ridingStatus) {
		return ridingStatus != null ? ridingPost.ridingParticipantSection.status.eq(ridingStatus) :
			null;
	}

	private BooleanExpression zoneEq(Integer zoneCode) {
		return zoneCode != null ? ridingPost.ridingMainSection.addressCode.code.eq(zoneCode) : null;
	}

	private BooleanExpression bicycleEq(Long bicycleCode) {
		return bicycleCode != null ? ridingConditionBicycle.bicycle.id.eq(bicycleCode)
			.or(ridingConditionBicycle.bicycle.id.eq(Bicycle.BicycleCode.ALL)) : null;
	}

	@Override
	public List<RidingPostBriefInfoQueryDslProjection> searchRidingPostByUser(User user,
		RidingPostUserSearchType searchType) {
		JPQLQuery<RidingPostBriefInfoQueryDslProjection> query = from(ridingPost)
			.leftJoin(ridingPost.leader)
			.leftJoin(ridingPost.thumbnail)
			.where(ridingPost.userConditionOf(user, searchType))
			.orderBy(ridingPost.ridingMainSection.ridingDate.desc(), ridingPost.id.desc())
			.limit(MAXIMUM_USER_SEARCH_RESULT)
			.select(new QRidingPostBriefInfoQueryDslProjection(
				ridingPost.id, ridingPost.ridingMainSection.title,
				ridingPost.thumbnail.url,
				ridingPost.ridingConditionSection.level.stringValue(), ridingPost.ridingMainSection.ridingDate,
				ridingPost.ridingMainSection.departurePlace));
		List<RidingPostBriefInfoQueryDslProjection> result = query.fetch();

		return result;
	}

	public List<RidingPostBriefInfoQueryDslProjection> searchEvaluabledRidingPostByUser(User user) {
		JPQLQuery<RidingPostBriefInfoQueryDslProjection> query = from(ridingPost)
			.leftJoin(ridingPost.leader)
			.leftJoin(ridingPost.thumbnail)
			.leftJoin(ridingParticipant).on(ridingParticipant.user.id.eq(user.getId())
				.and(ridingParticipant.post.id.eq(ridingPost.id))).fetchJoin()
			// .leftJoin(ridingParticipant.user).on(ridingParticipant.user.id.eq(user.getId()).and().fetchJoin()
			.where(ridingPost.userConditionOf(user, PARTICIPATED).and(
				ridingPost.participantConditionOf(ridingParticipant, user)
			))
			.orderBy(ridingPost.ridingMainSection.ridingDate.desc())
			.limit(MAXIMUM_USER_SEARCH_RESULT)
			.select(new QRidingPostBriefInfoQueryDslProjection(
				ridingPost.id, ridingPost.ridingMainSection.title,
				ridingPost.thumbnail.url,
				ridingPost.ridingConditionSection.level.stringValue(), ridingPost.ridingMainSection.ridingDate,
				ridingPost.ridingMainSection.departurePlace));

		List<RidingPostBriefInfoQueryDslProjection> result = query.fetch();
		return result;
	}

	public List<RidingPost> searchRidingPostInProgress() {
		JPQLQuery<RidingPost> query = from(ridingPost)
			.where(ridingPost.ridingParticipantSection.status.eq(RidingStatus.IN_PROGRESS))
			.orderBy(ridingPost.createdAt.asc())
			.limit(50).select(ridingPost);

		List<RidingPost> result = query.fetch();
		return result;
	}
}

