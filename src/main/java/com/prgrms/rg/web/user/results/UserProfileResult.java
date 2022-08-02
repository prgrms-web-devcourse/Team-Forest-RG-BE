package com.prgrms.rg.web.user.results;

import com.prgrms.rg.domain.user.model.information.MannerInfo;
import com.prgrms.rg.domain.user.model.information.UserProfilePageInfo;

import lombok.Data;

@Data
public class UserProfileResult {

	private String nickname;
	private RiderInfoResult profile;
	private ImageInfoResult image;
	private String introduction;
	private MannerInfo manner;

	private UserProfileResult(String nickname, RiderInfoResult profile, ImageInfoResult image,
		String introduction,
		MannerInfo manner) {
		this.nickname = nickname;
		this.profile = profile;
		this.image = image;
		this.introduction = introduction;
		this.manner = manner;
	}

	public static UserProfileResult of(UserProfilePageInfo info) {
		return new UserProfileResult(info.getNickname(), RiderInfoResult.of(info.getProfile()),
			ImageInfoResult.of(info.getImage()), info.getIntroduction(), info.getManner());
	}
}
