package com.prgrms.rg.web.ridingpost.requests;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Size;

import com.prgrms.rg.domain.ridingpost.application.command.RidingConditionSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingMainSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingParticipantSaveCommand;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RidingPostSaveRequest {

	private RidingSaveMainRequest information;

	@Size(min = 0, max = 5)
	private List<RidingSaveDetailRequest> details;

	public RidingSaveCommand toCommand() {
		var mainCommand =
			RidingMainSaveCommand.builder()
				.title(information.getTitle()).estimatedTime(information.getEstimatedTime())
				.ridingDate(information.getRidingDate()).fee(information.getFee())
				.addressCode(new AddressCode(information.getRegionCode()))
				.routes(information.getRoutes())
				.departurePlace(information.getDeparturePlace())
				.build();

		var participantCommand = new RidingParticipantSaveCommand
			(information.getMinParticipantCount(), information.getMaxParticipantCount());

		var conditionCommand = new RidingConditionSaveCommand(information.getLevel(), information.getBicycleTypes());

		var subCommandList =
			details.stream().map(RidingSaveDetailRequest::toCommand).collect(Collectors.toList());

		return new RidingSaveCommand(information.getThumbnail(),
			mainCommand, participantCommand, conditionCommand, subCommandList);
	}
}
