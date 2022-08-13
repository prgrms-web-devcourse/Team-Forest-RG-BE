package com.prgrms.rg.config;

import static org.apache.commons.lang3.RandomUtils.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.common.file.model.TemporaryImage;
import com.prgrms.rg.domain.common.file.model.TemporaryImageRepository;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.ridingpost.application.RidingPostService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingConditionSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingMainSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingParticipantSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSubSaveCommand;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;
import com.prgrms.rg.domain.user.model.Introduction;
import com.prgrms.rg.domain.user.model.Manner;
import com.prgrms.rg.domain.user.model.Nickname;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;

@Component
@Profile("!test")
public class DataInitializer {

		static final List<Integer> addressCodes = List.of(
			11,
		21,
		22,
		23,
		24,
		25,
		26,
		29,
		31,
		32,
		33,
		34,
		35,
		36,
		37,
		38,
		39,
		11010,
		11020,
		11030,
		11040,
		11050,
		11060,
		11070,
		11080,
		11090,
		11100,
		11110,
		11120,
		11130,
		11140,
		11150,
		11160,
		11170,
		11180,
		11190,
		11200,
		11210,
		11220,
		11230,
		11240,
		11250,
		21010,
		21020,
		21030,
		21040,
		21050,
		21060,
		21070,
		21080,
		21090,
		21100,
		21110,
		21120,
		21130,
		21140,
		21150,
		21310,
		22010,
		22020,
		22030,
		22040,
		22050,
		22060,
		22070,
		22310,
		23010,
		23020,
		23040,
		23050,
		23060,
		23070,
		23080,
		23090,
		23310,
		23320,
		24010,
		24020,
		24030,
		24040,
		24050,
		25010,
		25020,
		25030,
		25040,
		25050,
		26010,
		26020,
		26030,
		26040,
		26310,
		29010,
		31010,
		31011,
		31012,
		31013,
		31014,
		31020,
		31021,
		31022,
		31023,
		31030,
		31040,
		31041,
		31042,
		31050,
		31060,
		31070,
		31080,
		31090,
		31091,
		31092,
		31100,
		31101,
		31103,
		31104,
		31110,
		31120,
		31130,
		31140,
		31150,
		31160,
		31170,
		31180,
		31190,
		31191,
		31192,
		31193,
		31200,
		31210,
		31220,
		31230,
		31240,
		31250,
		31260,
		31270,
		31280,
		31350,
		31370,
		31380,
		32010,
		32020,
		32030,
		32040,
		32050,
		32060,
		32070,
		32310,
		32320,
		32330,
		32340,
		32350,
		32360,
		32370,
		32380,
		32390,
		32400,
		32410,
		33020,
		33030,
		33040,
		33041,
		33042,
		33043,
		33044,
		33320,
		33330,
		33340,
		33350,
		33360,
		33370,
		33380,
		33390,
		34010,
		34011,
		34012,
		34020,
		34030,
		34040,
		34050,
		34060,
		34070,
		34080,
		34310,
		34330,
		34340,
		34350,
		34360,
		34370,
		34380,
		35010,
		35011,
		35012,
		35020,
		35030,
		35040,
		35050,
		35060,
		35310,
		35320,
		35330,
		35340,
		35350,
		35360,
		35370,
		35380,
		36010,
		36020,
		36030,
		36040,
		36060,
		36310,
		36320,
		36330,
		36350,
		36360,
		36370,
		36380,
		36390,
		36400,
		36410,
		36420,
		36430,
		36440,
		36450,
		36460,
		36470,
		36480,
		37010,
		37011,
		37012,
		37020,
		37030,
		37040,
		37050,
		37060,
		37070,
		37080,
		37090,
		37100,
		37310,
		37320,
		37330,
		37340,
		37350,
		37360,
		37370,
		37380,
		37390,
		37400,
		37410,
		37420,
		37430,
		38030,
		38050,
		38060,
		38070,
		38080,
		38090,
		38100,
		38110,
		38111,
		38112,
		38113,
		38114,
		38115,
		38310,
		38320,
		38330,
		38340,
		38350,
		38360,
		38370,
		38380,
		38390,
		38400,
		39010,
		39020,
		99999
		);

	/*
		배포 서버 테스트용 기본 데이터
		// TODO: 실제 배포 환경에서는 삭제해야 한다.
		모집 여부
		바이크 종류
		지역
		난이도(level)
	 */

	private static User createUser(UserRepository userRepository, String username) {
		var user = User.builder()
			.nickname(new Nickname(username))
			.manner(Manner.create())
			.introduction(new Introduction("안녕하세요 " + username + "입니다."))
			.build();
		return user = userRepository.save(user);
	}

	private  void insertRidingPost(ConfigurableApplicationContext context, String bikeType, int addressCode, RidingLevel ridingLevel, User leader) {

		var temporaryImageRepository = context.getBean(TemporaryImageRepository.class);
		var ridingPostService = context.getBean(RidingPostService.class);
		var postRepository = context.getBean(RidingPostRepository.class);

		var tempThumbnailImage = new TemporaryImage("RG_Logo.png", "https://team-05-storage.s3.ap-northeast-2.amazonaws.com/static/RG_Logo.png");
		var thumbnailId = temporaryImageRepository.save(tempThumbnailImage).getId();
		var tempSubImage = new TemporaryImage("rogo2.png", "https://team-05-storage.s3.ap-northeast-2.amazonaws.com/static/09be3f01-f808-41a6-aadb-0cd63c57a7c6.png");
		var subImageId = temporaryImageRepository.save(tempSubImage).getId();

		List<String> routes = List.of("서울특별시", "경기도");
		var mainCreateCommand = RidingMainSaveCommand.builder()
			.title("Riding_" + nextInt())
			.estimatedTime(nextInt(1,6) + "시간").ridingDate(LocalDateTime.now().plusDays(nextLong(1, 20)))
			.fee(nextBoolean() ? 10000 : 0).addressCode(new AddressCode(addressCode)).routes(routes).build();
		var subCommand = new RidingSubSaveCommand("sub-title", "sub-content", List.of(subImageId));

		var minParticipant = nextInt(3, 10);
		var maxParticipant = minParticipant + nextInt(0, 5);
		var createCommand = new RidingSaveCommand(thumbnailId,
			mainCreateCommand,
			new RidingParticipantSaveCommand(minParticipant, maxParticipant),
			new RidingConditionSaveCommand(ridingLevel.getLevelName(),List.of(bikeType)),
			List.of(subCommand)
		);

		Long savedPostId = ridingPostService.createRidingPost(leader.getId(), createCommand);
		var post = postRepository.findById(savedPostId).get();
	}

	@EventListener(ApplicationReadyEvent.class)
	@Transactional
	public void insertData(ApplicationReadyEvent event) {
		List<User> userList = new LinkedList<>();
		var bicycleList = List.of("MTB",
			"TSB",
			"따릉이",
			"로드",
			"상관없음",
			"픽시",
			"하이브리드"
		);

		var userNames = List.of("Bob민성","Matt승범","Partey훈기","Pray민재","Kant한빈","Kiko채우","Tree인수","Didi현정");
		for (int i = 0; i < userNames.size(); i++) {
			userList.add(createUser(event.getApplicationContext().getBean(UserRepository.class), userNames.get(i)));

		}

		for (int i = 0; i < 1000; i++) {
			insertRidingPost(
				event.getApplicationContext(),
				bicycleList.get(nextInt(0, bicycleList.size()))
				, addressCodes.get(nextInt(0, addressCodes.size())),
				RidingLevel.values()[nextInt(0,RidingLevel.values().length)],
				userList.get(nextInt(0,userList.size())));
		}
	}
	}
