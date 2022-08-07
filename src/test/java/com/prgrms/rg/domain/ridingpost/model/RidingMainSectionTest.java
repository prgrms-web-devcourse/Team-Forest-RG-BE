package com.prgrms.rg.domain.ridingpost.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RidingMainSectionTest {

	@Autowired
	private AddressCodeFinder addressFinder;

	@Test
	@DisplayName("DB에 addresscode 존재 여부 테스트")
	void checkAddressCodeTest() {
		var mainSection = RidingMainSection.builder()
			.title("check-address")
			.addressCode(new AddressCode(99999))
			.ridingDate(LocalDateTime.now().plusDays(10L))
			.fee(0)
			.estimatedTime("2시간")
			.routes(List.of("start", "end"))
			.departurePlace(new Coordinate(123.3424324, 123.3424324)).build();

		assertThrows(IllegalArgumentException.class, () -> mainSection.checkAddressCode(addressFinder));
	}
}