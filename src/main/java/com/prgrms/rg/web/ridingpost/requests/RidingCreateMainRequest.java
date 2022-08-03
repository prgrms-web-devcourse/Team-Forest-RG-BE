package com.prgrms.rg.web.ridingpost.requests;

import java.time.LocalDateTime;
import java.util.List;

import com.prgrms.rg.domain.ridingpost.model.Coordinate;

import lombok.Data;

@Data
public class RidingCreateMainRequest {

	private String title;
	private LocalDateTime ridingDate;
	private int estimatedTime;
	private List<String> routes;
	private int fee;
	private int minParticipantCount;
	private int maxParticipantCount;
	private int regionCode;
	//사용해도 ㄱㅊ나
	private Coordinate departurePlace;
	private String level;
	private List<String> bicycleTypes;
	private Long thumbnail;

}
