package com.prgrms.rg.infrastructure.repository.projections.querydsl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prgrms.rg.domain.ridingpost.application.information.RidingPostBriefInfo;
import com.prgrms.rg.domain.ridingpost.model.Coordinate;
import com.prgrms.rg.domain.ridingpost.model.RidingConditionBicycle;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RidingPostBriefInfoQueryDslProjection implements RidingPostBriefInfo {

	private Long id;
	private String title;
	private String thumbnail;
	private String ridingLevel;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime ridingDate;
	private List<String> ridingCourses;
	private List<String> bicycleType;
	private Coordinate departurePosition;

	@QueryProjection
	public RidingPostBriefInfoQueryDslProjection(Long id, String title, String thumbnail, String ridingLevel,
		LocalDateTime ridingDate,
		List<String> ridingCourses, List<RidingConditionBicycle> bicycleTypes, Coordinate departurePosition) {
		this.id = id;
		this.title = title;
		this.thumbnail = thumbnail;
		this.ridingLevel = ridingLevel;
		this.ridingDate = ridingDate;
		this.ridingCourses = ridingCourses;
		this.bicycleType = new ArrayList<>();
		bicycleTypes.forEach((b) -> bicycleType.add(b.getBicycleName()));
		this.departurePosition = departurePosition;
	}
}
