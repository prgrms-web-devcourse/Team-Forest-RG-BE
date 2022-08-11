package com.prgrms.rg.domain.user.model.information;

import java.util.Collections;
import java.util.List;

import com.prgrms.rg.domain.common.model.metadata.RidingLevel;

import lombok.Getter;

public final class RiderInfo {

	@Getter
	private int ridingYears;
	@Getter
	private RidingLevel level;
	private List<String> bicycles;

	public RiderInfo(int ridingYears, RidingLevel level, List<String> bicycles) {
		this.ridingYears = ridingYears;
		this.level = level;
		this.bicycles = bicycles;
	}

	public List<String> getBicycles() {
		return Collections.unmodifiableList(bicycles);
	}

	public String[] getBicyclesAsArray() {
		String[] result = new String[bicycles.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = bicycles.get(i);
		}
		return result;
	}

	@Override
	public String toString() {
		return "RiderInfo{" +
			"ridingYears=" + ridingYears +
			", level=" + level +
			", bicycles=" + bicycles +
			'}';
	}
}
