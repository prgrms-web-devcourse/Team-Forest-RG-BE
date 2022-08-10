package com.prgrms.rg.domain.ridingpost.model;

import lombok.Data;

@Data
public class RidingSearchCondition {
	private Long bicycleType;
	private Integer zone;
	private String ridingLevel;
	private String postStatus;
}
