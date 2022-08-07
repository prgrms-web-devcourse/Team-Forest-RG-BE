package com.prgrms.rg.domain.user.application.impl;

import static com.prgrms.rg.domain.common.model.metadata.RidingLevel.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.common.model.metadata.Bicycle;
import com.prgrms.rg.domain.common.model.metadata.BicycleRepository;
import com.prgrms.rg.domain.user.application.UserCommandService;
import com.prgrms.rg.domain.user.application.command.UserUpdateCommand;
import com.prgrms.rg.domain.user.model.Introduction;
import com.prgrms.rg.domain.user.model.Manner;
import com.prgrms.rg.domain.user.model.Nickname;
import com.prgrms.rg.domain.user.model.RiderProfile;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;

@Transactional
@SpringBootTest
class UserCommandServiceImplTest {

	@Autowired
	private UserCommandService sut;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BicycleRepository bicycleRepository;

	@PersistenceContext
	EntityManager em;

	@Test
	@DisplayName("유저 정보를 수정할 수 있음: 성공 케이스")
	void edit_user() {
		//Given
		Bicycle road = new Bicycle(1L, "로드");
		Bicycle seoulBike = new Bicycle(2L, "따릉이");
		Bicycle mtb = new Bicycle(3L, "MTB");
		bicycleRepository.save(road);
		bicycleRepository.save(seoulBike);
		bicycleRepository.save(mtb);

		User user = userRepository.save(User.builder()
			.nickname(new Nickname("RGRider"))
			.profile(new RiderProfile(5, MASTER))
			.introduction(new Introduction("한강 라이딩을 즐겨합니다."))
			.manner(Manner.create())
			.build());

		user.addBicycle(road);
		user.addBicycle(seoulBike);

		UserUpdateCommand command = new UserUpdateCommand(user.getId(), "changedName", 3
			, BEGINNER.name(), new String[] {"따릉이", "MTB"}, "반갑습니다.");

		em.flush();
		em.clear();

		//When
		Long editedUserId = sut.edit(command);

		em.flush();
		em.clear();

		User afterEdited = userRepository.findById(editedUserId).get();

		//Then
		assertThat(afterEdited.getNickname()).isEqualTo("changedName");
		assertThat(afterEdited.getRiderInformation().getRidingYears()).isEqualTo(3);
		assertThat(afterEdited.getRiderInformation().getLevel()).isEqualTo(BEGINNER);
		List<String> changedBicycles = afterEdited.getRiderInformation().getBicycles();
		assertThat(changedBicycles).containsAll(List.of("따릉이", "MTB")).doesNotContain("로드");
		assertThat(afterEdited.getIntroduction()).isEqualTo("반갑습니다.");
	}
}