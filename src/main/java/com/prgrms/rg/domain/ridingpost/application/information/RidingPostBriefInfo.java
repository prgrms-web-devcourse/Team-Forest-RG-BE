package com.prgrms.rg.domain.ridingpost.application.information;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.ridingpost.model.Coordinate;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RidingPostBriefInfo {

	private Long id;
	private String title;
	private String thumbnail;
	private String ridingLevel;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime ridingDate;
	private Coordinate departurePosition;
	private ZoneInfo zone;
	private List<String> bicycles;
	private String estimatedTime;
	private List<String> ridingCourses;

	public RidingPostBriefInfo(Long id, String title, String thumbnail, String ridingLevel, LocalDateTime ridingDate,
		Coordinate departurePosition, ZoneInfo zone, List<String> bicycles, String estimatedTime,
		List<String> ridingCourses) {
		this.id = id;
		this.title = title;
		this.thumbnail = thumbnail;
		this.ridingLevel = ridingLevel;
		this.ridingDate = ridingDate;
		this.departurePosition = departurePosition;
		this.zone = zone;
		this.bicycles = bicycles;
		this.estimatedTime = estimatedTime;
		this.ridingCourses = ridingCourses;
	}

	public static RidingPostBriefInfo from(RidingPost post) {
		return new RidingPostBriefInfo(post.getId(), post.getRidingMainSection().getTitle(),
			post.getThumbnail(), post.getRidingConditionSection().getLevel(),
			post.getRidingMainSection().getRidingDate(),
			post.getRidingMainSection().getDeparturePlace(),
			ZoneInfo.from(post.getRidingMainSection().getAddressCode()),
			post.getRidingConditionSection().getBicycleAsStringList(), post.getRidingMainSection().getEstimatedTime(),
			post.getRidingMainSection().getRoutes());
	}

	@Data
	@NoArgsConstructor
	public static class ZoneInfo {
		private int code;
		private String name;

		public ZoneInfo(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public static ZoneInfo from(AddressCode addressCode) {
			return new ZoneInfo(addressCode.getCode(), addressCode.toString());

		}
	}
}