package com.prgrms.rg.testutil;

import java.time.LocalDateTime;
import java.util.List;

import com.prgrms.rg.domain.common.file.model.TemporaryImage;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
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

}
