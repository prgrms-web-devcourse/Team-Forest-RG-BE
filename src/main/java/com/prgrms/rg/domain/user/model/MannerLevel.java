package com.prgrms.rg.domain.user.model;

public enum MannerLevel {
	//TODO: 명칭 통일
	NORMAL, FOUR_LEGS_BICYCLE, THREE_LEGS_BICYCLE, TWO_LEGS_BICYCLE, LEGENDARY_RIDER;

	private static final short[] REQUIRED_FOR_PROMOTION = new short[] {500, 1000, 2000, 4000};

	public static MannerLevel of(short point) {
		if (point >= REQUIRED_FOR_PROMOTION[3]) {
			return LEGENDARY_RIDER;
		}
		if (point >= REQUIRED_FOR_PROMOTION[2]) {
			return TWO_LEGS_BICYCLE;
		}
		if (point >= REQUIRED_FOR_PROMOTION[1]) {
			return THREE_LEGS_BICYCLE;
		}
		if (point >= REQUIRED_FOR_PROMOTION[0]) {
			return FOUR_LEGS_BICYCLE;
		}
		return NORMAL;
	}
}
