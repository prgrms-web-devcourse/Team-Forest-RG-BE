package com.prgrms.rg.domain.user.model;

import static lombok.AccessLevel.*;

import java.time.Year;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import lombok.NoArgsConstructor;

/**
 * 해당 유저의 라이더로서의 정보들을 모아 둔 클래스
 * 몇년 차 라이더인지(ridingStartYear), 수준은 어느 정도인지(level), 어떤 종류의 자전거를 소유하는지(UserBicycles) 보관
 */
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class RiderProfile {
	private Year ridingStartYear;
	private RidingLevel level;
	@Embedded
	private UserBicycles bicycles;

	/*	Tree와의 논의에서 자전거 경력을 입력 쪽에서는 "5년차" 형태로 받고,
		서버 쪽에서는 그것을 시작 년도로 치환, 저장하여 사용자 편의성을 높이기로 하였음
	 */
	public RiderProfile(int ridingYears, RidingLevel level, UserBicycles bicycles) {
		this.ridingStartYear = Year.now().minusYears(ridingYears);
		this.level = level;
		this.bicycles = bicycles;
	}
}
