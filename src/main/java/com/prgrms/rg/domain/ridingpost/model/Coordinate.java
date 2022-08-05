package com.prgrms.rg.domain.ridingpost.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coordinate {

	@Column(name = "lng")
	private double lng;

	@Column(name = "lat")
	private double lat;

}
