package com.prgrms.rg.domain.ridingpost.application.command;

import java.util.List;

import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.ridingpost.model.RidingConditionSection;

import lombok.Data;

@Data
public class RidingConditionSaveCommand {

	private final String level;
	private final List<String> bicycleTypes;

	public RidingConditionSection toSection() {
		return new RidingConditionSection(RidingLevel.valueOf(level));
	}
}
