package com.prgrms.rg.web.ridingpost.requests;

import java.util.List;
import java.util.stream.Collectors;

import com.prgrms.rg.domain.ridingpost.application.command.RidingConditionCreateCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingCreateCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingMainCreateCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingParticipantCreateCommand;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RidingPostCreateRequest {

	private RidingCreateMainRequest information;

	private List<RidingCreateDetailRequest> details;

	public RidingCreateCommand toCommand() {
		var mainCommand =
			RidingMainCreateCommand.builder()
				.title(information.getTitle()).estimatedTime(information.getEstimatedTime())
				.ridingDate(information.getRidingDate()).fee(information.getFee())
				.addressCode(new AddressCode(information.getRegionCode()))
				.routes(information.getRoutes())
				.departurePlace(information.getDeparturePlace())
				.build();

		var participantCommand = new RidingParticipantCreateCommand
			(information.getMinParticipantCount(), information.getMaxParticipantCount());

		var conditionCommand = new RidingConditionCreateCommand(information.getLevel(), information.getBicycleTypes());

		var subCommandList =
			details.stream().map(RidingCreateDetailRequest::toCommand).collect(Collectors.toList());

		return new RidingCreateCommand(information.getThumbnail(),
			mainCommand, participantCommand, conditionCommand, subCommandList);
	}
}
