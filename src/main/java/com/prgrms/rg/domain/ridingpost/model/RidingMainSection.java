package com.prgrms.rg.domain.ridingpost.model;

import static com.google.common.base.Preconditions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PRIVATE)
public class RidingMainSection {

	//1-30차
	@Size(min = 1, max = 30)
	@Column(name = "title", nullable = false)
	private String title;

	@Min(value = 0L)
	@Column(name = "estimated_minutes", nullable = false)
	private int estimatedMinutes;

	@Column(name = "riding_date", nullable = false)
	private LocalDateTime ridingDate;

	@Min(value = 0)
	@Column(name = "fee")
	private int fee = 0;

	@JoinColumn(name = "address_code", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private AddressCode addressCode;

	@ElementCollection
	@CollectionTable(name = "riding_routes", joinColumns =
	@JoinColumn(name = "post_id"))
	@OrderColumn(name = "list_idx")
	@Column(name = "route")
	private List<String> routes = new ArrayList<>();

	@Embedded
	private Coordinate departurePlace;

	@Builder
	public RidingMainSection(String title, int estimatedMinutes, LocalDateTime ridingDate, int fee,
		AddressCode addressCode, List<String> routes, Coordinate departurePlace) {
		setTitle(title);
		setEstimatedMinutes(estimatedMinutes);
		setFee(fee);
		setAddressCode(addressCode);
		setRidingDate(ridingDate);
		setRoutes(routes);
		setDeparturePlace(departurePlace);
	}

	public List<String> getRoutes() {
		return Collections.unmodifiableList(routes);
	}

	//작성 날짜보다 미래인지
	private void setRidingDate(LocalDateTime ridingDate) {
		checkArgument(ridingDate.isAfter(LocalDateTime.now()));
		this.ridingDate = ridingDate;
	}
}
