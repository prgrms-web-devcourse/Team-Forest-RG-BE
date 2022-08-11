package com.prgrms.rg.infrastructure.repository;

import static com.prgrms.rg.domain.common.model.metadata.QBicycle.*;
import static com.prgrms.rg.domain.ridingpost.model.QRidingConditionBicycle.*;
import static com.prgrms.rg.domain.ridingpost.model.QRidingPost.*;

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
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class QuerydslRidingPostSearchRepository extends QuerydslRepositorySupport
	implements RidingPostSearchRepository {

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
				postStatusEq(condition.getRidingStatus()),
				zoneEq(condition.getAddressCode()),
				bicycleEq(condition.getBicycleCode())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1L)
			.select(ridingPost).distinct();

		Objects.requireNonNull(getQuerydsl()).applySorting(pageable.getSort(), query);

		List<RidingPost> result = query.fetch();
		log.error("query result size : " + result.size());

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

	private BooleanExpression postStatusEq(RidingStatus ridingStatus) {
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
}

