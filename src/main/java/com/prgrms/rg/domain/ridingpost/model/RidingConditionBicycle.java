package com.prgrms.rg.domain.ridingpost.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.prgrms.rg.domain.common.model.metadata.Bicycle;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RidingConditionBicycle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private RidingPost post;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Bicycle bicycle;

	public RidingConditionBicycle(RidingPost post, Bicycle bicycle) {
		this.post = post;
		this.bicycle = bicycle;
	}

	public String getBicycleName() {
		return bicycle.getName();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RidingConditionBicycle))
			return false;

		RidingConditionBicycle that = (RidingConditionBicycle)o;
		return bicycle.equals(that.bicycle);
	}

	@Override
	public int hashCode() {
		return bicycle.hashCode();
	}
}
