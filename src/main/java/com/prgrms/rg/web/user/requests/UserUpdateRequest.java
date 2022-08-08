package com.prgrms.rg.web.user.requests;

import com.prgrms.rg.domain.user.application.command.UserUpdateCommand;
import com.prgrms.rg.domain.user.model.QNickname;

import lombok.Data;

@Data
public class UserUpdateRequest {

	private String nickname;
	private int ridingYears;
	private String ridingLevel;
	private String[] bicycles;
	private String introduction;

	public UserUpdateCommand from(Long id) {
		return new UserUpdateCommand(id, nickname, ridingYears, ridingLevel, bicycles, introduction);
	}
}
