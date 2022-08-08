package com.prgrms.rg.domain.ridingpost.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.rg.domain.user.model.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class RidingPostInfo {
	@JsonProperty(value = "leader")
	private LeaderInfo leaderInfo;
	@JsonProperty(value = "riding")
	private RidingInfo ridingInfo;

	private RidingPostInfo(RidingPost ridingPost) {
		this.leaderInfo = new LeaderInfo(ridingPost.getLeader());
		this.ridingInfo = RidingInfo.from(ridingPost);
	}

	public static RidingPostInfo from(RidingPost post) {
		return new RidingPostInfo(post);
	}

	@Getter
	public static class LeaderInfo {
		private final Long id;
		private final String nickname;
		private final String profileImage;

		public LeaderInfo(User leader) {
			this.id = leader.getId();
			this.nickname = leader.getNickname();
			this.profileImage = leader.getImage().getFileUrl();
		}
	}

	@Getter
	@Builder
	public static class RidingInfo {
		private String title;
		private String thumbnail;
		private String ridingLevel;
		private ZoneInfo zone;
		private int fee;
		private String estimatedTime;
		private LocalDateTime createdAt;
		private LocalDateTime ridingDate;
		private List<String> bicycleType;
		private List<String> ridingCourses;
		private int maxParticipant;
		private int minParticipant;
		private List<ParticipantInfo> participants;
		private Coordinate departurePosition;
		private List<RidingDetail> details;

		public static RidingInfo from(RidingPost ridingPost) {
			RidingMainSection mainSection = ridingPost.getRidingMainSection();
			RidingConditionSection conditionSection = ridingPost.getRidingConditionSection();
			RidingParticipantSection participantSection = ridingPost.getRidingParticipantSection();
			List<RidingSubSection> subSectionList = ridingPost.getSubSectionList();
			List<RidingDetail> details = subSectionList.stream().map(RidingDetail::from).collect(Collectors.toList());
			List<ParticipantInfo> participantInfos = participantSection.getParticipants().stream()
				.map(ridingParticipant -> {
					return ParticipantInfo.from(ridingParticipant.getUser());
				}).collect(Collectors.toList());
			return RidingInfo.builder()
				.title(mainSection.getTitle())
				.thumbnail(ridingPost.getThumbnail())
				.ridingLevel(conditionSection.getLevel())
				.ridingDate(mainSection.getRidingDate())
				.zone(ZoneInfo.from(mainSection.getAddressCode()))
				.bicycleType(conditionSection.getBicycleAsStringList())
				.fee(mainSection.getFee())
				.estimatedTime(mainSection.getEstimatedTime())
				.createdAt(ridingPost.getCreatedAt())
				.ridingCourses(mainSection.getRoutes())
				.maxParticipant(participantSection.getMaxParticipantCount())
				.minParticipant(participantSection.getMinParticipantCount())
				.participants(participantInfos)
				.departurePosition(mainSection.getDeparturePlace())
				.details(details)
				.build();
		}
	}

	@Builder
	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class RidingDetail {
		private Long id;
		private String title;
		private String contents;
		private List<String> images;

		public static RidingDetail from(RidingSubSection ridingSubSection) {
			return RidingDetail.builder()
				.id(ridingSubSection.getId())
				.title(ridingSubSection.getTitle())
				.contents(ridingSubSection.getContent())
				.images(ridingSubSection.getImageUrlAsList())
				.build();
		}
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ParticipantInfo {
		private final Long id;
		private final String nickname;
		private final String profileImage;

		public static ParticipantInfo from(User user) {
			return new ParticipantInfo(user.getId(), user.getNickname(), user.getImage().getFileUrl());
		}
	}

	@Getter
	@AllArgsConstructor
	public static class ZoneInfo {
		private final int code;
		private final String name;

		public static ZoneInfo from(AddressCode addressCode) {
			return new ZoneInfo(addressCode.getCode(), addressCode.toString());
		}
	}
}
