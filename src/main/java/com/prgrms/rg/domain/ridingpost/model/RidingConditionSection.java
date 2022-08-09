package com.prgrms.rg.domain.ridingpost.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import com.prgrms.rg.domain.common.model.metadata.Bicycle;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@Setter(value = AccessLevel.PRIVATE)
public class RidingConditionSection {

	@Enumerated(EnumType.STRING)
	@Column(name = "level", nullable = false)
	private RidingLevel level;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private Set<RidingConditionBicycle> bicycleList = new HashSet<>();

	//todo post-conditionsection mapping 여기서 ?
	public RidingConditionSection(RidingLevel level) {
		setLevel(level);
	}

	public void addBicycle(RidingPost post, Bicycle bicycle) {
		bicycleList.add(new RidingConditionBicycle(post, bicycle));
	}

	public List<String> getBicycleAsStringList() {
		return bicycleList.stream()
			.map(RidingConditionBicycle::getBicycleName)
			.collect(Collectors.toList());
	}

	public String getLevel() {
		return level.getLevelName();
	}

	private void setBicycleList(RidingPost post, List<Bicycle> bicycleList) {
		for (Bicycle bicycle : bicycleList) {
			addBicycle(post, bicycle);
		}
	}
}
