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

	@Column(name = "estimated_time", nullable = false)
	private String estimatedTime;

	@Column(name = "riding_date", nullable = false)
	private LocalDateTime ridingDate;

	@Column(name = "evaluation_due_date", nullable = false)
	private LocalDateTime evaluationDueDate;

	@Min(value = 0)
	@Column(name = "fee")
	private int fee = 0;

	@JoinColumn(name = "address_code", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private AddressCode addressCode;

	//0-5개 길이 제한
	@ElementCollection
	@CollectionTable(name = "riding_routes", joinColumns =
	@JoinColumn(name = "post_id"))
	@OrderColumn(name = "list_idx")
	@Column(name = "route")
	private List<String> routes = new ArrayList<>();

	@Embedded
	private Coordinate departurePlace;

	@Builder
	public RidingMainSection(String title, String estimatedTime, LocalDateTime ridingDate, int fee,
		AddressCode addressCode, List<String> routes, Coordinate departurePlace) {
		setTitle(title);
		setEstimatedTime(estimatedTime);
		setFee(fee);
		assignAddressCode(addressCode);
		setRidingDate(ridingDate);
		setRoutes(routes);
		setDeparturePlace(departurePlace);
	}

	public void update(RidingMainSection section) {
		setTitle(section.getTitle());
		setEstimatedTime(section.getEstimatedTime());
		setFee(section.getFee());
		assignAddressCode(section.getAddressCode());
		setRidingDate(section.getRidingDate());
		setDeparturePlace(section.getDeparturePlace());
		routes.clear();
		routes.addAll(section.getRoutes());
	}

	public List<String> getRoutes() {
		return Collections.unmodifiableList(routes);
	}

	//작성 날짜보다 미래인지
	private void setRidingDate(LocalDateTime ridingDate) {
		checkArgument(ridingDate.isAfter(LocalDateTime.now().minusDays(1L)));
		this.ridingDate = ridingDate;
		this.evaluationDueDate = ridingDate.plusDays(7L);
	}

	public void assignAddressCode(AddressCode code) {
		this.addressCode = code;
	}

}
