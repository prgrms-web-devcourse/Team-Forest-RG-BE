package com.prgrms.rg.domain.ridingpost.application.command;

import java.util.List;

import lombok.Data;

@Data
public class RidingSaveCommand {

	private final Long thumbnailId;

	private final RidingMainSaveCommand mainCommand;

	//참여자 정보
	private final RidingParticipantSaveCommand participantCommand;

	//조건(bicycle, level)
	private final RidingConditionSaveCommand conditionCommand;

	//sub-section
	private final List<RidingSubSaveCommand> subCommand;
}
