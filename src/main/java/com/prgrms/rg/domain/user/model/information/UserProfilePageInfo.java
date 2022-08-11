package com.prgrms.rg.domain.user.model.information;

import com.prgrms.rg.domain.user.model.User;

import lombok.Getter;

@Getter
public class UserProfilePageInfo {

	private String nickname;
	private RiderInfo profile;
	private UserImageInfo image;
	private String introduction;
	private MannerInfo manner;

	private ContactInfo contact;

	public UserProfilePageInfo(String nickname, RiderInfo profile,
		UserImageInfo image, String introduction, MannerInfo manner, ContactInfo contact) {
		this.nickname = nickname;
		this.profile = profile;
		this.image = image;
		this.introduction = introduction;
		this.manner = manner;
		this.contact = contact;
	}

	public static UserProfilePageInfo of(User user) {
		return new UserProfilePageInfo(user.getNickname(), user.getRiderInformation(),
			user.getImage(), user.getIntroduction(), user.getMannerInfo(), user.getContactInfo());
	}
}
