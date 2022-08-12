package com.prgrms.rg.infrastructure.repository.projections.querydsl;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

@Data
public class RidingBicyclesInfoQueryDslProjection {
	private Long postId;
	private String bicycleName;

	@QueryProjection
	public RidingBicyclesInfoQueryDslProjection(Long postId, String bicycleName) {
		this.postId = postId;
		this.bicycleName = bicycleName;
	}
}
