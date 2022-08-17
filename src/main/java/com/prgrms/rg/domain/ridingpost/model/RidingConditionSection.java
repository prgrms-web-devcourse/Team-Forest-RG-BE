package com.prgrms.rg.domain.ridingpost.model;

import java.util.Collections;
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

import org.springframework.util.CollectionUtils;

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

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<RidingConditionBicycle> bicycleList = new HashSet<>();

	public RidingConditionSection(RidingLevel level) {
		setLevel(level);
	}

	public void update(RidingPost post, RidingConditionSection section) {
		setLevel(level);
		bicycleList.clear();
		if (!CollectionUtils.isEmpty(section.getBicycleList())) {
			for (RidingConditionBicycle conditionBicycle : section.getBicycleList()) {
				addBicycle(post, conditionBicycle.getBicycle());
			}
		}
	}

	public void addBicycle(RidingPost post, Bicycle bicycle) {
		bicycleList.add(new RidingConditionBicycle(post, bicycle));
	}

	public Set<RidingConditionBicycle> getBicycleList() {
		return Collections.unmodifiableSet(bicycleList);
	}

	public List<String> getBicycleAsStringList() {
		return bicycleList.stream()
			.map(RidingConditionBicycle::getBicycleName)
			.collect(Collectors.toList());
	}

	public String getLevel() {
		return level.getLevelName();
	}
}
