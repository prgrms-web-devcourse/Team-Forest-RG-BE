package com.prgrms.rg.domain.user.model;

import static lombok.AccessLevel.*;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

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
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserBicycle> bicycles = new HashSet<>();

	/*	Tree와의 논의에서 자전거 경력을 입력 쪽에서는 "5년차" 형태로 받고,
		서버 쪽에서는 그것을 시작 년도로 치환, 저장하여 사용자 편의성을 높이기로 하였음
	 */
	//TODO: User 생성자에서 UserBicycle 추가 불가능?
	RiderProfile(int ridingYears, RidingLevel level) {
		this.ridingStartYear = Year.now().minusYears(ridingYears);
		this.level = level;
	}

	boolean addBicycle(User user, Bicycle bicycle) {
		return bicycles.add(new UserBicycle(user, bicycle));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("RiderProfile{")
			.append("\n\tridingStartYear=").append(ridingStartYear)
			.append("\n\tlevel=").append(level)
			.append("\n\t bicycles=");
		bicycles.stream().forEach((b) -> sb.append(b.toString()).append(", "));
		sb.delete(sb.length() - 2, sb.length() - 1);
		sb.append("\n}");
		return sb.toString();
	}
}