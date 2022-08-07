package com.prgrms.rg.domain.user.application.command;

import com.prgrms.rg.domain.user.model.User;

import lombok.Getter;

@Getter
public class UserUpdateCommand {
	private Long id;
	private String nickname;
	private int ridingYears;
	private String ridingLevel;
	private String[] bicycles;
	private String introduction;

	public UserUpdateCommand(Long id, String nickname, int ridingYears, String ridingLevel, String[] bicycles,
		String introduction) {
		this.id = id;
		this.nickname = nickname;
		this.ridingYears = ridingYears;
		this.ridingLevel = ridingLevel;
		this.bicycles = bicycles;
		this.introduction = introduction;
	}
}
