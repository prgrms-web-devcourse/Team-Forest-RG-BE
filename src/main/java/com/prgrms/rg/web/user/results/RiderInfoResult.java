package com.prgrms.rg.web.user.results;

import java.util.List;

import com.prgrms.rg.domain.user.model.information.RiderInfo;

import lombok.Data;

@Data
public final class RiderInfoResult {

	private Integer ridingYears;
	private String level;
	private List<String> bicycles;

	private RiderInfoResult(Integer ridingYears, String level, List<String> bicycles) {
		this.ridingYears = ridingYears;
		this.level = level;
		this.bicycles = bicycles;
	}

	public static RiderInfoResult of(RiderInfo riderInfo) {
		return new RiderInfoResult(riderInfo.getRidingYears(), riderInfo.getLevel().toString(),
			riderInfo.getBicycles());
	}
}
