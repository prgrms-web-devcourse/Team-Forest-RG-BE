package com.prgrms.rg.web.user.results;

import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.information.MannerInfo;

import lombok.Data;

@Data
public class UserInfoResult {

	private String nickname;
	private RiderInfoResult profile;
	private ImageInfoResult image;
	private String introduction;
	private MannerInfo manner;

	private UserInfoResult(String nickname, RiderInfoResult profile, ImageInfoResult image,
		String introduction,
		MannerInfo manner) {
		this.nickname = nickname;
		this.profile = profile;
		this.image = image;
		this.introduction = introduction;
		this.manner = manner;
	}

	public static UserInfoResult of(User user) {
		return new UserInfoResult(user.getNickname(), RiderInfoResult.of(user.getRiderInformation()),
			ImageInfoResult.of(user.getImage()), user.getIntroduction(), user.getMannerInfo());
	}
}
