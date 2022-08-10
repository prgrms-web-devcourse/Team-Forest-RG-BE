package com.prgrms.rg.domain.user.application.command;

import java.util.List;

import com.prgrms.rg.web.user.requests.UserRegisterRequest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRegisterCommand {
	private int ridingStartYear;
	private int favoriteRegionCode;
	private List<String> bicycles;
	private String level;
	private String phoneNumber;
	private String nickName;
	private Long userId;

	public static UserRegisterCommand of(UserRegisterRequest userRegisterRequest,Long userId) {
		UserRegisterCommand command = new UserRegisterCommand();
		command.setRidingStartYear(userRegisterRequest.getRidingStartYear());
		command.setFavoriteRegionCode(userRegisterRequest.getFavoriteRegionCode());
		command.setBicycles(userRegisterRequest.getBicycles());
		command.setLevel(userRegisterRequest.getLevel());
		command.setPhoneNumber(userRegisterRequest.getPhoneNumber());
		command.setUserId(userId);
		command.setNickName(userRegisterRequest.getNickName());
		return command;
	}
}
