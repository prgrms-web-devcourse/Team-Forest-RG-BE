package com.prgrms.rg.domain.user.model;

import static com.google.common.base.Preconditions.*;
import static lombok.AccessLevel.*;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Nickname {

	@Column(nullable = false, unique = true, length = 16)
	private String nickname;

	private static final short NICKNAME_MINIMAL_BYTE_LENGTH = 6;
	private static final short NICKNAME_MAXIMUM_BYTE_LENGTH = 24;
	private static final String LENGTH_VALIDATION_MESSAGE = "닉네임은 " + NICKNAME_MINIMAL_BYTE_LENGTH + "바이트 이상, "
		+ NICKNAME_MAXIMUM_BYTE_LENGTH + "이하여야 합니다. 현재 바이트 수: ";
	private static final Pattern CHARSET_REGEX = Pattern.compile("(^[ㄱ-ㅎ가-힣a-zA-Z]*$)");
	private static final String ALLOWED_CHARSET = "한글과 알파벳";
	private static final String CHARSET_VALIDATION_MESSAGE = "닉네임은 " + ALLOWED_CHARSET + "만 허용됩니다.";

	public Nickname(String nickname) {
		validateNicknameLength(nickname);
		validateNicknameCharacter(nickname);
		this.nickname = nickname;
	}

	String get() {
		return nickname;
	}

	private void validateNicknameLength(String nickname) {
		int length = nickname.getBytes(StandardCharsets.UTF_8).length;
		checkArgument(length >= NICKNAME_MINIMAL_BYTE_LENGTH
			&& length <= NICKNAME_MAXIMUM_BYTE_LENGTH, LENGTH_VALIDATION_MESSAGE + length);
	}

	private void validateNicknameCharacter(String nickname) {
		checkArgument(CHARSET_REGEX.matcher(nickname).matches(), CHARSET_VALIDATION_MESSAGE);
	}

	@Override
	public String toString() {
		return nickname;
	}
}
