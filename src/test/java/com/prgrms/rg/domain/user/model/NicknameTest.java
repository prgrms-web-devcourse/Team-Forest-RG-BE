package com.prgrms.rg.domain.user.model;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("닉네임은")
class NicknameTest {

	@ParameterizedTest
	@CsvSource({"한글", "제일긴한글닉네임", "English", "RG라이더"})
	@DisplayName("한글, 영문 6 ~ 24바이트 이내면 정상 생성된다. 성공 케이스: ")
	void validate_Nickname_Success(String nicknameArg) {
		Nickname generatedNickname = new Nickname(nicknameArg);
		assertThat(generatedNickname).isNotNull();
	}

	@ParameterizedTest
	@CsvSource({"a", "가", "가나다라마바사아자차", "thisIsVeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeryLongId",
		"정말긴아이디입니다AndAlphabet"})
	@DisplayName("길이가 6 ~ 24바이트 범위를 벗어나면 생성되지 않는다. 실패 케이스: ")
	void validate_Nickname_FailWithLength(String nicknameArg) {
		assertThrows(IllegalArgumentException.class, () -> new Nickname(nicknameArg));
	}

	@ParameterizedTest
	@CsvSource({"가나 다라", "ab@!", "가a@나다", "Übermensch", "プログラミング", "編程"})
	@DisplayName("한글 및 알파벳이 아닌 문자 혹은 공백을 포함할 경우 생성되지 않는다. 실패 케이스: ")
	void validate_Nickname_FailWithCharset(String nicknameArg) {
		assertThrows(IllegalArgumentException.class, () -> new Nickname(nicknameArg));
	}
}