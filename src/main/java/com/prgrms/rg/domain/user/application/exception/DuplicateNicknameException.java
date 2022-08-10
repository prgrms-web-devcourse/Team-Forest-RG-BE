package com.prgrms.rg.domain.user.application.exception;

import com.prgrms.rg.domain.user.model.Nickname;

public class DuplicateNicknameException extends IllegalArgumentException {
	public DuplicateNicknameException(Nickname nickname) {
		super("닉네임 \"" + nickname + "\"은 다른 회원이 사용중이기에, 사용할 수 없습니다.");
	}
}
