package com.prgrms.rg.web.ridingpost.requests;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Pattern;

import com.prgrms.rg.domain.ridingpost.model.Coordinate;

import lombok.Data;

@Data
public class RidingCreateMainRequest {

	private String title;
	private LocalDateTime ridingDate;

	// todo ~시 ~분 @Pattern()
	private String estimatedTime;
	private List<String> routes;
	private int fee;
	private int minParticipantCount;
	private int maxParticipantCount;
	private int regionCode;
	private Coordinate departurePlace;
	private String level;
	private List<String> bicycleTypes;
	private Long thumbnail;

}
