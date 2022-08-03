package com.prgrms.rg.domain.ridingpost.application.command;

import java.util.List;

import lombok.Data;

@Data
public class RidingCreateCommand {

	private final Long thumbnailId;

	//main
	private final RidingMainCreateCommand mainCommand;

	//participant
	private final RidingParticipantCreateCommand participantCommand;

	//condition
	private final RidingConditionCreateCommand conditionCommand;

	//sub
	private final List<RidingSubCreateCommand> subCommand;
}
