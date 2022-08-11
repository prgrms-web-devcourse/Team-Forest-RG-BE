package com.prgrms.rg.domain.ridingpost.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RidingSearchCondition {
	private Long bicycleCode;
	private Integer addressCode;
	private String ridingLevel;
	private Long ridingStatusCode = RidingStatusCode.PROGRESS;
	private RidingStatus ridingStatus;

	public void convertRidingStatusFromCode() {
		switch (ridingStatusCode.intValue()) {
			case 0:
				ridingStatus = null;
				break;
			case 1:
				ridingStatus = RidingStatus.IN_PROGRESS;
				break;
			case 2:
				ridingStatus = RidingStatus.CLOSED;
				break;
			default:
				throw new IllegalArgumentException(
					"Riding Status Code '" + ridingStatusCode.intValue() + "' is invalid");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class RidingStatusCode {
		public static final Long ALL = 0L;
		public static final Long PROGRESS = 1L;
		public static final Long CLOSED = 2L;

	}
}
