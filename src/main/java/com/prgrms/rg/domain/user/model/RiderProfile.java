package com.prgrms.rg.domain.user.model;

import static javax.persistence.EnumType.*;
import static lombok.AccessLevel.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import com.prgrms.rg.domain.common.model.metadata.Bicycle;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.user.model.information.RiderInfo;

import lombok.NoArgsConstructor;

/**
 * 해당 유저의 라이더로서의 정보들을 모아 둔 클래스
 * 몇년 차 라이더인지(ridingStartYear), 수준은 어느 정도인지(level), 어떤 종류의 자전거를 소유하는지(UserBicycles) 보관
 */
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class RiderProfile {
	private Year ridingStartYear;

	@Enumerated(value = STRING)
	private RidingLevel level;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserBicycle> bicycles = new HashSet<>();

	/*	Tree와의 논의에서 자전거 경력을 입력 쪽에서는 "5년차" 형태로 받고,
		서버 쪽에서는 그것을 시작 년도로 치환, 저장하여 사용자 편의성을 높이기로 하였음
	 */
	//TODO: User 생성자에서 UserBicycle 추가 불가능?

	public RiderProfile(int ridingStartYear, RidingLevel level) {
		this.ridingStartYear = Year.of(ridingStartYear);
		this.level = level;
	}

	public RiderProfile(int ridingStartYear, RidingLevel level, Set<UserBicycle> bicycles) {
		this.ridingStartYear = Year.of(ridingStartYear);
		this.level = level;
		this.bicycles = bicycles;
	}

	public void update(int ridingStartYear, RidingLevel level, Set<UserBicycle> bicyclesToApply) {
		this.ridingStartYear = Year.of(ridingStartYear);
		this.level = level;
		bicycles.retainAll(bicyclesToApply);
		bicycles.addAll(bicyclesToApply);
	}

	public Set<UserBicycle> getBicycles() {
		return Collections.unmodifiableSet(bicycles);
	}

	boolean addBicycle(User user, Bicycle bicycle) {
		return bicycles.add(new UserBicycle(user, bicycle));
	}

	Year getRidingYears() {
		return ridingStartYear;
	}

	RiderInfo information() {
		List<String> bicycleNames = new ArrayList<>();
		for (UserBicycle bicycle : bicycles) {
			bicycleNames.add(bicycle.getName());
		}
		return new RiderInfo(getRidingYears().getValue(), level, bicycleNames);
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
