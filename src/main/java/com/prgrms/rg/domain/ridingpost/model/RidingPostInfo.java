package com.prgrms.rg.domain.ridingpost.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.rg.domain.user.model.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	@Setter(AccessLevel.PRIVATE)
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class RidingInfo {
		private Long id;
		private String title;
		private String thumbnail;
		private String ridingLevel;
		private ZoneInfo zone;
		private int fee;
		private String estimatedTime;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
		private LocalDateTime createdAt;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
		private LocalDateTime ridingDate;
		private List<String> bicycleType;
		private List<String> ridingCourses;
		private int maxParticipant;
		private int minParticipant;
		private List<ParticipantInfo> participants;
		private Coordinate departurePosition;
		private List<RidingDetail> details;

		public static RidingInfo from(RidingPost ridingPost) {
			RidingInfo instance = new RidingInfo();
			RidingMainSection mainSection = ridingPost.getRidingMainSection();
			mapMainSection(mainSection, instance);
			RidingConditionSection conditionSection = ridingPost.getRidingConditionSection();
			mapConditionSection(conditionSection, instance);
			RidingParticipantSection participantSection = ridingPost.getRidingParticipantSection();
			mapParticipantSection(participantSection, instance);
			List<RidingSubSection> subSectionList = ridingPost.getSubSectionList();
			mapSubSection(subSectionList, instance);
			instance.setId(ridingPost.getId());
			instance.setThumbnail(ridingPost.getThumbnail());
			instance.setCreatedAt(ridingPost.getCreatedAt());
			return instance;
		}

		private static void mapMainSection(RidingMainSection mainSection, RidingInfo instance) {
			if (mainSection == null)
				return;
			List<String> loadedCourse = new ArrayList<>(mainSection.getRoutes());
			instance.setTitle(mainSection.getTitle());
			instance.setRidingDate(mainSection.getRidingDate());
			instance.setFee(mainSection.getFee());
			instance.setRidingCourses(loadedCourse);
			instance.setDeparturePosition(mainSection.getDeparturePlace());
			instance.setZone(ZoneInfo.from(mainSection.getAddressCode()));
			instance.setEstimatedTime(mainSection.getEstimatedTime());
		}

		private static void mapConditionSection(RidingConditionSection conditionSection, RidingInfo instance) {
			if (conditionSection == null)
				return;
			instance.setRidingLevel(conditionSection.getLevel());
			instance.setBicycleType((conditionSection.getBicycleAsStringList()));
		}

		private static void mapParticipantSection(RidingParticipantSection participantSection, RidingInfo instance) {
			if (participantSection == null)
				return;
			List<ParticipantInfo> participantInfos = participantSection.getParticipants().stream()
				.map(ridingParticipant -> ParticipantInfo.from(ridingParticipant.getUser()))
				.collect(Collectors.toList());

			instance.setMaxParticipant((participantSection.getMaxParticipantCount()));
			instance.setMinParticipant(participantSection.getMinParticipantCount());
			instance.setParticipants((participantInfos));
		}

		private static void mapSubSection(List<RidingSubSection> subSectionList, RidingInfo instance) {
			List<RidingDetail> details = subSectionList.stream().map(RidingDetail::from).collect(Collectors.toList());
			instance.setDetails(details);
		}
	}

	@Builder
	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class RidingDetail {
		private Long id;
		private String title;
		private String contents;
		private List<ImageInfo> images;

		public static RidingDetail from(RidingSubSection ridingSubSection) {
			List<SubImage> imageList = ridingSubSection.getImages();
			List<ImageInfo> imageInfos = imageList.stream()
				.map(ImageInfo::from)
				.collect(Collectors.toList());

			return RidingDetail.builder()
				.id(ridingSubSection.getId())
				.title(ridingSubSection.getTitle())
				.contents(ridingSubSection.getContent())
				.images(imageInfos)
				.build();
		}
	}

	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Getter
	public static class ImageInfo {
		private final Long id;
		private final String imageUrl;

		public static ImageInfo from(SubImage subImage) {
			return new ImageInfo(subImage.getId(), subImage.getUrl());
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
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ZoneInfo {
		private final int code;
		private final String name;

		public static ZoneInfo from(AddressCode addressCode) {
			return new ZoneInfo(addressCode.getCode(), addressCode.toString());
		}
	}
}
