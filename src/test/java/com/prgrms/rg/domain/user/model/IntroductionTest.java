package com.prgrms.rg.domain.user.model;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import lombok.extern.slf4j.Slf4j;

@DisplayName("자기소개는 ")
class IntroductionTest {
	@ParameterizedTest
	@CsvSource({"짧은 소개글",
		"가나다라마바사아자차가나다라마바사아자차가나다라마바사아자차가나다라마바사아자차가나다라마바사아자차가나다라마바사아자차가나다라마바사아자차가나다라마바사아자차바사아자a",
		"abcdefghijklmnopqrstuvwxyz!@# abcdefghijklmnopqrstuvwxyz!@# abcdefghijklmnopqrstuvwxyz!@# abcdefghijklmnopqrstuvwxyz!@#"})
	@DisplayName("255 바이트 이내면 정상 생성된다. 성공 케이스: ")
	void validate_Introduction_Success(String introductionArg) {
		Introduction generatedIntroduction = new Introduction(introductionArg);
		assertThat(generatedIntroduction).isNotNull();
	}

	@ParameterizedTest
	@CsvSource({
		"가나다라마바사아자차가나다라마바사아자차가나다라마바사아자차가나다라마바사아자차가나다라마바사아자차가나다라마바사아자차가나다라마바사아자차가나다라마바사아자차바사아자차가",
		"abcdefghijklmnopqrstuvwxyz!@# abcdefghijklmnopqrstuvwxyz!@# abcdefghijklmnopqrstuvwxyz!@# abcdefghijklmnopqrstuvwxyz!@# 자차가나다라마바사아자차바사아자차가자차가나다라마바사아자차바사아자차가자차가나다라마바사아자차바사아자차가"})
	@DisplayName("길이가 255바이트를 초과하면 생성되지 않는다. 실패 케이스: ")
	void validate_Introduction_FailWithLength(String introductionArg) {
		assertThrows(IllegalArgumentException.class, () -> new Introduction(introductionArg));
	}

}