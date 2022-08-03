package com.prgrms.rg.domain.ridingpost.application.command;

import java.util.ArrayList;
import java.util.List;

import com.prgrms.rg.domain.common.model.metadata.Bicycle;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.ridingpost.model.RidingConditionSection;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;

import lombok.Data;

@Data
public class RidingConditionCreateCommand {

	private final String level;
	private final List<String> bicycleTypes;

	public RidingConditionSection toSection(RidingPost post) {
		List<Bicycle> bicycleList = new ArrayList<>();
		for (String typeName : bicycleTypes) {
			bicycleList.add(Bicycle.of(typeName));
		}

		//ridingyear 제거
		return new RidingConditionSection(post, RidingLevel.valueOf(level), 0, bicycleList);
	}
}
