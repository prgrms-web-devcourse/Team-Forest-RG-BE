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
}
