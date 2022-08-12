package com.prgrms.rg.web.user.results;

import java.time.LocalDate;

import com.prgrms.rg.domain.user.model.information.ParticipatedRidingInfo;
import com.prgrms.rg.domain.user.model.information.UserProfilePageInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileResult {

	private PrivacyProfileResult privacyProfile;
	private RiderProfileResult ridingProfile;
	private MannerResult manner;
	private ParticipatedRidingInfo ridings;

	private UserProfileResult(PrivacyProfileResult privacyProfile, RiderProfileResult ridingProfile,
		MannerResult manner, ParticipatedRidingInfo ridings) {
		this.privacyProfile = privacyProfile;
		this.ridingProfile = ridingProfile;
		this.manner = manner;
		this.ridings = ridings;
	}

	public static UserProfileResult of(UserProfilePageInfo info) {
		return new UserProfileResult(
			new PrivacyProfileResult(info.getContact().getPhoneNumber(), info.getContact().getEmail()),
			new RiderProfileResult(info.getNickname(), info.getImage().getFileUrl(), info.getIntroduction(),
				info.getProfile().getRidingYears(),
				info.getProfile().getLevel().getLevelName(), info.getProfile().getBicyclesAsArray()),
			new MannerResult(info.getManner().getMannerPoint(), info.getManner().getNoShow(),
				info.getManner().getBannedUntil()),
			info.getRidings());
	}

	@Data
	private static class PrivacyProfileResult {

		public PrivacyProfileResult() {
		}

		public PrivacyProfileResult(String phoneNumber, String kakaoEmail) {
			this.phoneNumber = phoneNumber;
			this.kakaoEmail = kakaoEmail;
		}

		private String phoneNumber;
		private String kakaoEmail;
	}

	@Data
	private static class RiderProfileResult {

		public RiderProfileResult() {
		}

		private String nickname;
		private String profileImage;
		private String introduction;
		private int ridingStartYear;
		private String level;
		private String[] bicycles;

		private RiderProfileResult(String nickname, String profileImage, String introduction, int ridingStartYear,
			String level,
			String[] bicycles) {
			this.nickname = nickname;
			this.profileImage = profileImage;
			this.introduction = introduction;
			this.ridingStartYear = ridingStartYear;
			this.level = level;
			this.bicycles = bicycles;
		}
	}

	@Data
	private static class MannerResult {

		public MannerResult() {
		}

		private int mannerPoint;
		private int noShow;
		private LocalDate banned;

		private MannerResult(int mannerPoint, int noShow, LocalDate banned) {
			this.mannerPoint = mannerPoint;
			this.noShow = noShow;
			this.banned = banned;
		}
	}
}
