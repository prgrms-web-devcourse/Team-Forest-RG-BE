package com.prgrms.rg.domain.ridingpost.application.command;

import java.util.List;

import lombok.Data;

@Data
public class RidingCreateCommand {

	private final Long thumbnailId;

	private final RidingMainCreateCommand mainCommand;

	//참여자 정보
	private final RidingParticipantCreateCommand participantCommand;

	//조건(bicycle, level)
	private final RidingConditionCreateCommand conditionCommand;

	//sub-section
	private final List<RidingSubCreateCommand> subCommand;
}
