package com.prgrms.rg.testutil;

import java.time.LocalDateTime;
import java.util.List;

import com.prgrms.rg.domain.common.file.model.TemporaryImage;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.ridingpost.application.command.RidingConditionSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingMainSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingParticipantSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSaveCommand;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.ridingpost.model.Coordinate;
import com.prgrms.rg.domain.ridingpost.model.RidingConditionSection;
import com.prgrms.rg.domain.ridingpost.model.RidingMainSection;
import com.prgrms.rg.domain.ridingpost.model.RidingParticipantSection;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingSubSection;
import com.prgrms.rg.domain.ridingpost.model.SubImage;
import com.prgrms.rg.domain.user.model.Manner;
import com.prgrms.rg.domain.user.model.Nickname;
import com.prgrms.rg.domain.user.model.User;

/*
 * 테스트용 도메인 엔티티를 생성하는 팩토리 클래스
 *
 * */
public class TestEntityDataFactory {
	public static int ADDRESS_CODE = 11010;

	public static User createUser(Long id, String nickname) {
		return User.builder()
			.id(id)
			.nickname(new Nickname(nickname))
			.manner(Manner.create())
			.build();
	}

	public static User createUser(Long id) {
		return User.builder()
			.id(id)
			.nickname(new Nickname("testUser"))
			.manner(Manner.create())
			.build();
	}

	public static User createUser() {
		return User.builder()
			.nickname(new Nickname("testUser"))
			.manner(Manner.create())
			.build();
	}

	public static User createUser(String nickname) {
		return User.builder()
			.nickname(new Nickname(nickname))
			.manner(Manner.create())
			.build();
	}

	public static RidingPost createRidingPost(Long leaderId) {
		User leader = createUser(leaderId);
		RidingConditionSection conditionSection = new RidingConditionSection(RidingLevel.MASTER);
		RidingParticipantSection participantSection = new RidingParticipantSection(2, 5);
		RidingSubSection subSection = new RidingSubSection("sub title", "sub contents");
		TemporaryImage tempImage = new TemporaryImage("test_image_file.jpg",
			"http://amazons3/static/image/testimage.jpg");
		subSection.getImages().add(new SubImage(1L, tempImage, subSection));

		RidingMainSection mainSection = RidingMainSection.builder()
			.title("자전거가 타고싶어요")
			.ridingDate(LocalDateTime.now().plusDays(10L))
			.routes(List.of("중앙 공원", "능골 공원", "탑골 공원"))
			.estimatedTime("120분")
			.addressCode(new AddressCode(ADDRESS_CODE))
			.fee(10000)
			.departurePlace(new Coordinate(35.232600, 127.650250))
			.build();

		RidingPost post = RidingPost.builder()
			.leader(leader)
			.ridingMainSection(mainSection)
			.ridingConditionSection(conditionSection)
			.ridingParticipantSection(participantSection)
			.build();

		post.addSubSection(subSection);
		return post;
	}

	public static RidingSaveCommand createRidingPostCreateCommand(List<String> bicycle, String level, int zoneCode,
		Long leaderId) {
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

		return new RidingSaveCommand(null,
			mainCreateCommand,
			new RidingParticipantSaveCommand(minPart, maxPart),
			new RidingConditionSaveCommand(level, bicycle),
			null
		);
	}

	public static RidingSaveCommand createRidingPostCreateCommandWithParticipant(int minPart, int maxPart) {
		String title = "testTitle";
		String estimatedTime = "120";
		LocalDateTime ridingDate = LocalDateTime.now().plusDays(10L);
		int fee = 0;
		AddressCode addressCode = new AddressCode(11010);
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

		return new RidingSaveCommand(null,
			mainCreateCommand,
			new RidingParticipantSaveCommand(minPart, maxPart),
			new RidingConditionSaveCommand("상", List.of("MTB", "픽시")),
			null
		);
	}

	public static RidingSaveCommand createRidingPostCreateCommandWithRidingDate(LocalDateTime ridingDate) {
		String title = "testTitle";
		String estimatedTime = "120";
		int fee = 0;
		AddressCode addressCode = new AddressCode(11010);
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

		return new RidingSaveCommand(null,
			mainCreateCommand,
			new RidingParticipantSaveCommand(1, 3),
			new RidingConditionSaveCommand("상", List.of("MTB", "픽시")),
			null
		);
	}

}
