package com.prgrms.rg.domain.user.model.information;

import com.prgrms.rg.domain.user.model.User;

import lombok.Getter;

@Getter
public class UserProfilePageInfo {

	private String nickname;
	private RiderInfo profile;
	private UserImageInfo image;
	private String introduction;
	public Integer regionCode;
	private MannerInfo manner;
	private ContactInfo contact;
	private ParticipatedRidingInfo ridings;

	private UserProfilePageInfo(String nickname, RiderInfo profile,
		UserImageInfo image, String introduction, Integer regionCode, MannerInfo manner, ContactInfo contact,
		ParticipatedRidingInfo ridings) {
		this.nickname = nickname;
		this.profile = profile;
		this.image = image;
		this.introduction = introduction;
		this.regionCode = regionCode;
		this.manner = manner;
		this.contact = contact;
		this.ridings = ridings;
	}

	public static UserProfilePageInfo from(User user, ParticipatedRidingInfo ridings) {
		return new UserProfilePageInfo(user.getNickname(), user.getRiderInformation(),
			user.getImage(), user.getIntroduction(), user.getRegionCode(), user.getMannerInfo(), user.getContactInfo(), ridings);
	}
}
