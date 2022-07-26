package com.prgrms.rg.domain.ridingpost.application.command;

import com.prgrms.rg.domain.ridingpost.model.RidingParticipantSection;

import lombok.Data;

@Data
public class RidingParticipantSaveCommand {

	private final int minParticipantCount;
	private final int maxParticipantCount;

	public RidingParticipantSection toSection() {
		return new RidingParticipantSection(minParticipantCount, maxParticipantCount);
	}
}
