package com.prgrms.rg.web.user.requests;

import com.prgrms.rg.domain.user.application.command.UserUpdateCommand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

	private String nickname;
	private int ridingYears;
	private String ridingLevel;
	private String[] bicycles;
	private String introduction;

	public UserUpdateCommand toCommand(Long id) {
		return new UserUpdateCommand(id, nickname, ridingYears, ridingLevel, bicycles, introduction);
	}
}
