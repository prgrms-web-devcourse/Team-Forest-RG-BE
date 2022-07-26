package com.prgrms.rg.infrastructure.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.prgrms.rg.domain.common.model.metadata.Bicycle;
import com.prgrms.rg.domain.ridingpost.application.RidingPostService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingConditionSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingMainSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingParticipantSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSaveCommand;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.ridingpost.model.Coordinate;
import com.prgrms.rg.domain.ridingpost.model.RidingPostInfo;
import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;
import com.prgrms.rg.domain.ridingpost.model.RidingSearchCondition;
import com.prgrms.rg.domain.ridingpost.model.RidingStatus;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;
import com.prgrms.rg.testutil.TestEntityDataFactory;

@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:address_code.sql", "classpath:bicycle.sql"})
class QuerydslRidingPostSearchRepositoryTest {
	@Autowired
	private QuerydslRidingPostSearchRepository searchRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RidingPostService ridingPostService;
	@Autowired
	private ObjectMapper mapper;
	private final static Integer BUNDANG = 31023;
	private final static Integer GANGNAM = 11230;
	private final static Integer JONGRO = 11010;
	private final static Integer YOUNGSAN = 11030;
	private final static Integer GUANJIN = 11050;
	private final static Integer NAMYANGJU = 31130;

	@Autowired
	RidingPostRepository ridingPostRepository;

	@BeforeEach
	void init() {
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		User leader = TestEntityDataFactory.createUser();
		long userId = userRepository.save(leader).getId();

		List<String> mtv = List.of(Bicycle.BicycleName.MTB);
		List<String> mtvRoad = List.of(Bicycle.BicycleName.MTB, Bicycle.BicycleName.ROAD);
		List<String> fixie = List.of(Bicycle.BicycleName.FIXIE);

		for (int i = 0; i < 10; i++) {
			createPostWithCondition(mtv, "상", BUNDANG, userId);
		}

		for (int i = 0; i < 20; i++) {
			createPostWithCondition(mtvRoad, "중", BUNDANG, userId);
		}

		for (int i = 0; i < 5; i++) {
			createPostWithCondition(fixie, "하", GANGNAM, userId);
		}

		for (int i = 0; i < 4; i++) {
			createClosePostWithCondition(fixie, "중", JONGRO, userId);
		}

		for (int i = 0; i < 4; i++) {
			createClosePostWithCondition(fixie, "하", YOUNGSAN, userId);
			createClosePostWithCondition(fixie, "하", GUANJIN, userId);
		}
		for (int i = 0; i < 7; i++) {
			createClosePostWithCondition(fixie, "하", NAMYANGJU, userId);
		}
	}

	@DisplayName("지역별 라이딩 게시글 조회")
	@ParameterizedTest
	@CsvSource({
		"31023,   30",
		"11230,   5",
	})
	void test(int zone, int expect) {
		//given
		Pageable pageable = PageRequest.of(0, 50, Sort.Direction.DESC, "createdAt");
		RidingSearchCondition condition = new RidingSearchCondition();
		condition.setAddressCode(zone);

		//when
		Slice<RidingPostInfo> ridingPostInfos = searchRepository.searchRidingPostSlice(condition, pageable);

		//then
		List<RidingPostInfo> results = ridingPostInfos.getContent();
		System.out.println(results.get(0).getRidingInfo().getBicycleType().get(0));
		assertThat(results).hasSize(expect);
	}

	@DisplayName("시 코드로 라이딩 게시글 조회")
	@ParameterizedTest
	@CsvSource({
		"11,   17",
		"31,   37"
	})
	void test154(int zone, int expect) {
		//given
		Pageable pageable = PageRequest.of(0, 50, Sort.Direction.DESC, "createdAt");
		RidingSearchCondition condition = new RidingSearchCondition();
		condition.setAddressCode(zone);

		//when
		Slice<RidingPostInfo> ridingPostInfos = searchRepository.searchRidingPostSlice(condition, pageable);

		//then
		List<RidingPostInfo> results = ridingPostInfos.getContent();
		assertThat(results).hasSize(expect);
	}

	@DisplayName("숙련도별 라이딩 게시글 조회")
	@ParameterizedTest
	@CsvSource({
		"상,   10",
		"중,   24",
		"하,   20"
	})
	void test2(String level, int expect) {
		//given
		Pageable pageable = PageRequest.of(0, 50, Sort.Direction.DESC, "createdAt");
		RidingSearchCondition condition = new RidingSearchCondition();
		condition.setRidingLevel(level);

		//when
		Slice<RidingPostInfo> ridingPostInfos = searchRepository.searchRidingPostSlice(condition, pageable);

		//then
		List<RidingPostInfo> results = ridingPostInfos.getContent();
		assertThat(results).hasSize(expect);
	}

	@DisplayName("마감된 모든 라이딩 조회")
	@Test
	void test3() throws Exception {
		//given
		Pageable pageable = PageRequest.of(0, 50, Sort.Direction.DESC, "createdAt");
		RidingSearchCondition condition = new RidingSearchCondition();
		condition.setRidingStatus(RidingStatus.CLOSED);

		//when
		Slice<RidingPostInfo> ridingPostInfos = searchRepository.searchRidingPostSlice(condition, pageable);

		//then
		List<RidingPostInfo> results = ridingPostInfos.getContent();
		assertThat(results).hasSize(19);

		System.out.println(mapper.writeValueAsString(ridingPostInfos));
	}

	@DisplayName("현재 모집 진행중인 모든 라이딩 조회")
	@Test
	void test4() {
		//given
		Pageable pageable = PageRequest.of(0, 50, Sort.Direction.DESC, "createdAt");
		RidingSearchCondition condition = new RidingSearchCondition();
		condition.setRidingStatus(RidingStatus.IN_PROGRESS);

		//when
		Slice<RidingPostInfo> ridingPostInfos = searchRepository.searchRidingPostSlice(condition, pageable);

		//then
		List<RidingPostInfo> results = ridingPostInfos.getContent();
		assertThat(results).hasSize(35);
	}

	@DisplayName("MTB만 조회")
	@Test
	void test5() {
		//given
		Pageable pageable = PageRequest.of(0, 50, Sort.Direction.DESC, "createdAt");
		RidingSearchCondition condition = new RidingSearchCondition();
		condition.setBicycleCode(Bicycle.BicycleCode.MTB);

		//when
		Slice<RidingPostInfo> ridingPostInfos = searchRepository.searchRidingPostSlice(condition, pageable);

		//then
		List<RidingPostInfo> results = ridingPostInfos.getContent();
		for (RidingPostInfo result : results) {
			assertThat(result.getRidingInfo().getBicycleType()).contains(Bicycle.BicycleName.MTB);
		}
		assertThat(results).hasSize(30);
	}

	@DisplayName("자전거 타입, 라이딩 난이도 동시 적용")
	@ParameterizedTest
	@CsvSource({
		"1, 중, 20",
		"1, 상, 10",
	})
	void test6(Long code, String level, int expect) {
		//given
		Pageable pageable = PageRequest.of(0, 50, Sort.Direction.DESC, "createdAt");
		RidingSearchCondition condition = new RidingSearchCondition();
		condition.setBicycleCode(code);
		condition.setRidingLevel(level);

		//when
		Slice<RidingPostInfo> ridingPostInfos = searchRepository.searchRidingPostSlice(condition, pageable);

		//then
		List<RidingPostInfo> results = ridingPostInfos.getContent();
		for (RidingPostInfo result : results) {
			assertThat(result.getRidingInfo().getBicycleType()).contains(Bicycle.BicycleName.MTB);
		}
		assertThat(results).hasSize(expect);
	}

	private Long createPostWithCondition(List<String> bicycle, String level, int zoneCode, Long leaderId) {
		String title = "testTitle";
		String estimatedTime = "120";
		LocalDateTime ridingDate = LocalDateTime.now().plusDays(10L);
		int fee = 0;
		AddressCode addressCode = new AddressCode(zoneCode);
		int minPart = 4;
		int maxPart = 10;
		List<String> routes = List.of("start", "end");
		var mainCreateCommand = RidingMainSaveCommand.builder()
			.title(title)
			.estimatedTime(estimatedTime)
			.ridingDate(ridingDate)
			.fee(fee)
			.addressCode(addressCode)
			.routes(routes)
			.departurePlace(new Coordinate(37.660666, 126.229333))
			.build();

		var createCommand = new RidingSaveCommand(null,
			mainCreateCommand,
			new RidingParticipantSaveCommand(minPart, maxPart),
			new RidingConditionSaveCommand(level, bicycle), null
		);

		return ridingPostService.createRidingPost(leaderId, createCommand);
	}

	private Long createClosePostWithCondition(List<String> bicycle, String level, int zoneCode, Long leaderId) {
		String title = "testTitle";
		String estimatedTime = "120";
		LocalDateTime ridingDate = LocalDateTime.now().plusDays(10L);
		int fee = 0;
		AddressCode addressCode = new AddressCode(zoneCode);
		int minPart = 1;
		int maxPart = 1;
		List<String> routes = List.of("start", "end");
		var mainCreateCommand = RidingMainSaveCommand.builder()
			.title(title)
			.estimatedTime(estimatedTime)
			.ridingDate(ridingDate)
			.fee(fee)
			.addressCode(addressCode)
			.routes(routes)
			.departurePlace(new Coordinate(37.660666, 126.229333))
			.build();

		var createCommand = new RidingSaveCommand(null,
			mainCreateCommand,
			new RidingParticipantSaveCommand(minPart, maxPart),
			new RidingConditionSaveCommand(level, bicycle), null
		);

		return ridingPostService.createRidingPost(leaderId, createCommand);
	}

}