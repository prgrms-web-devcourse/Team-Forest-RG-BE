package com.prgrms.rg.domain.ridingpost.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.ridingpost.model.event.RidingJoinEvent;

@SpringBootTest
@RecordApplicationEvents
@Transactional
class RidingJoinServiceTest {
	@Autowired
	private RidingJoinService ridingJoinService;
	@Autowired
	private ApplicationEvents events;

	// 요청 처리 테스트 하고
	// 이벤트 발행 되는지 테스트
	@DisplayName("라이딩 참가 신청 시 RidingJoinEvent 발행되어야 한다")
	@Test
	void test() {
		//given

		//when
		ridingJoinService.joinUserToRiding(1L, 2L);

		//then
		long count = events.stream(RidingJoinEvent.class).count();
		assertThat(count).isEqualTo(1);

	}

}