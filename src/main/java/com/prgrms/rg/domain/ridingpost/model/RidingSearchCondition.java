package com.prgrms.rg.domain.ridingpost.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RidingSearchCondition {
	private Long bicycleCode;
	private Integer addressCode;
	private String ridingLevel;
	private Long ridingStatus = RidingStatusCode.PROGRESS;

	public RidingStatus getRidingStatusFromCode() {
		switch (ridingStatus.intValue()) {
			case 0:
				return null;
			case 1:
				return RidingStatus.IN_PROGRESS;
			case 2:
				return RidingStatus.CLOSED;
			default:
				throw new IllegalArgumentException("Riding Status Code '" + ridingStatus.intValue() + "' is invalid");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class RidingStatusCode {
		public static final Long ALL = 0L;
		public static final Long PROGRESS = 1L;
		public static final Long CLOSED = 2L;

	}
}
