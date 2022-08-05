package com.prgrms.rg.domain.common.model.metadata;

//TODO: Riding 관련 메타데이터는 어느 패키지로 가야할까?
public enum RidingLevel {
	BEGINNER("하", "라이딩에 익숙하지 않은 초보자"),
	INTERMEDIATE("중","일정 수준 이상의 라이딩 스킬을 가진 중급자"),
	MASTER("상","라이딩에 능숙한 고수");

	private final String name;

	private final String description;

	RidingLevel(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
