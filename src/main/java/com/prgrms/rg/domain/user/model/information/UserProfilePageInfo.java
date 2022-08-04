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
	//TODO: 라이딩 정보 로딩 후, 참여한 라이딩 횟수를 RiderInfoResult에 저장

	public UserProfilePageInfo(String nickname, RiderInfo profile,
		UserImageInfo image, String introduction, MannerInfo manner) {
		this.nickname = nickname;
		this.profile = profile;
		this.image = image;
		this.introduction = introduction;
		this.manner = manner;
	}

	public static UserProfilePageInfo of(User user) {
		return new UserProfilePageInfo(user.getNickname(), user.getRiderInformation(),
			user.getImage(), user.getIntroduction(), user.getMannerInfo());
	}
}
