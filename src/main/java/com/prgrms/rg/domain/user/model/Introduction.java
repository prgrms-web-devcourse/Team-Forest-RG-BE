package com.prgrms.rg.domain.user.model;

import static com.google.common.base.Preconditions.*;

import java.nio.charset.StandardCharsets;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Introduction {
	private static final short MAXIMUM_LENGTH = (short) 85;
	private static final String LENGTH_VALIDATION_MESSAGE = "소개글은 한글 기준 " + MAXIMUM_LENGTH + "자, 영문 알파벳 기준"
		+ MAXIMUM_LENGTH * 3 + "자 이하여야 합니다.";
	@Column
	private String introduction;

	Introduction(String introduction) {
		validateLength(introduction);
		this.introduction = introduction;
	}

	private void validateLength(String introduction) {
		int length = introduction.getBytes(StandardCharsets.UTF_8).length;
		checkArgument(length <= MAXIMUM_LENGTH * 3, LENGTH_VALIDATION_MESSAGE);
	}
}
