package com.prgrms.rg.domain.ridingpost.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

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

	@Min(value = 0)
	@Column(name = "riding_year", nullable = false)
	private int ridingYear;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private Set<RidingConditionBicycle> bicycleList;

	public RidingConditionSection(RidingPost post, RidingLevel level, int ridingYear, List<Bicycle> bicycleList) {
		setLevel(level);
		setRidingYear(ridingYear);
		setBicycleList(post, bicycleList);
	}

	public void addBicycle(RidingPost post, Bicycle bicycle) {
		bicycleList.add(new RidingConditionBicycle(post, bicycle));
	}

	private void setBicycleList(RidingPost post, List<Bicycle> bicycleList) {
		for (Bicycle bicycle : bicycleList) {
			addBicycle(post, bicycle);
		}
	}
}
