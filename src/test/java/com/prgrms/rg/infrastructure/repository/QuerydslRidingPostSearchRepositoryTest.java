package com.prgrms.rg.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.ridingpost.application.RidingPostService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingConditionSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingMainSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingParticipantSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSaveCommand;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.ridingpost.model.RidingPostInfo;
import com.prgrms.rg.domain.ridingpost.model.RidingSearchCondition;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;
import com.prgrms.rg.testutil.TestEntityDataFactory;

@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:address_code.sql", "classpath:bicycle.sql"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QuerydslRidingPostSearchRepositoryTest {
	@Autowired
	private QuerydslRidingPostSearchRepository searchRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RidingPostService ridingPostService;

	@BeforeAll
	void initData() {
		/*
		 * TYPE = MTB, ZONE = 경기도 성남시 분당구, LEVEL = "상" 10개
		 * TYPE = 로드,MTB ZONE = 경기도 성남시 분당구, LEVEL = "중" 10개
		 * TYPE = 픽시 ZONE = 서울특별시 강남구 LEVEL = "하" 10개
		 * */

		// List<String> mtv = List.of("MTB");
		// List<String> mtvRoad = List.of("MTB","로드");
		// List<String> fixie = List.of("픽시");
		//
		// for(int i=0;i<10;i++){
		// 	createPostWithCondition(mtv,"상",)
		// }

	}

	@DisplayName("자전거 타입이 MTB인 라이딩 게시글만 조회")
	@Test
	void test() {
		int zoneCde = 11010;
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
		RidingSearchCondition condition = new RidingSearchCondition();
		condition.setZone(zoneCde);
		condition.setBicycleType(1L);
		Long id = createPostWithCondition(List.of("MTB"), RidingLevel.MASTER.getLevelName(), zoneCde);
		Slice<RidingPostInfo> ridingPostInfos = searchRepository.searchRidingPostSlice(condition, pageable);
		System.out.println(ridingPostInfos.getContent().size());
		for (RidingPostInfo ridingPostInfo : ridingPostInfos) {
			System.out.println(ridingPostInfo.getRidingInfo().getTitle());
		}

	}

	@DisplayName("숙련도 '상'인 라이딩 게시글만 조회")
	@Test
	void test2() {

	}

	//
	// @DisplayName("경기도 성남시 분당에서 진행되는 라이딩 게시글만 조회")
	// @Test
	// void test2(){
	//
	// }
	private Long createPostWithCondition(List<String> bicycle, String level, int zoneCode) {
		String title = "testTitle";
		String estimatedTime = "120";
		LocalDateTime ridingDate = LocalDateTime.now().plusDays(10L);
		int fee = 0;
		AddressCode addressCode = new AddressCode(zoneCode);
		int minPart = 4;
		int maxPart = 10;
		List<String> routes = List.of("start", "end");
		User leader = TestEntityDataFactory.createUser();
		long userId = userRepository.save(leader).getId();
		var mainCreateCommand = RidingMainSaveCommand.builder()
			.title(title)
			.estimatedTime(estimatedTime)
			.ridingDate(ridingDate)
			.fee(fee)
			.addressCode(addressCode)
			.routes(routes)
			.build();

		var createCommand = new RidingSaveCommand(null,
			mainCreateCommand,
			new RidingParticipantSaveCommand(minPart, maxPart),
			new RidingConditionSaveCommand(level, bicycle), null
		);

		return ridingPostService.createRidingPost(userId, createCommand);
	}
}