package com.prgrms.rg.domain.ridingpost.application.command;

import java.time.LocalDateTime;
import java.util.List;

import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.ridingpost.model.Coordinate;
import com.prgrms.rg.domain.ridingpost.model.RidingMainSection;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RidingMainSaveCommand {

	private String title;

	private String estimatedTime;

	private LocalDateTime ridingDate;

	private int fee;

	private AddressCode addressCode;

	private List<String> routes;

	private Coordinate departurePlace;

	public RidingMainSection toSection() {
		return RidingMainSection.builder()
			.title(title)
			.estimatedTime(estimatedTime).ridingDate(ridingDate).fee(fee).addressCode(addressCode)
			.routes(routes).departurePlace(departurePlace).build();
	}
}
