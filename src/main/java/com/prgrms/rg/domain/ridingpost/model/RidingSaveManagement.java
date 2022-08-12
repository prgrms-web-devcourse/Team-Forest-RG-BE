package com.prgrms.rg.domain.ridingpost.model;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.prgrms.rg.domain.common.file.application.ImageAttachManger;
import com.prgrms.rg.domain.common.file.model.AttachedImageRepository;
import com.prgrms.rg.domain.common.model.metadata.Bicycle;
import com.prgrms.rg.domain.common.model.metadata.BicycleRepository;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSubSaveCommand;
import com.prgrms.rg.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RidingSaveManagement {

	private final ImageAttachManger imageManager;
	private final AddressCodeRepository addressCodeRepository;
	private final BicycleRepository bicycleRepository;
	private final AttachedImageRepository imageRepository;

	public RidingPost updateRidingPost(User leader, RidingPost post, RidingSaveCommand command) {

		//section
		var newPost = toPost(leader, command);
		post.changePost(newPost);

		//thumbnail -> 비교
		if (!post.equalToThumbnail(command.getThumbnailId())) {
			post.removeCurrentImage();
			//null이 아닌 썸네일 삽입
			if (!Objects.isNull(command.getThumbnailId())) {
				imageManager.store(command.getThumbnailId(), post);
			}
		}

		//subsection 새로 생성
		post.removeCurrentSubSection();
		if (!CollectionUtils.isEmpty(command.getSubCommand()) && command.getSubCommand().size() <= 5) {
			for (RidingSubSaveCommand subCommand : command.getSubCommand()) {
				var subSection = new RidingSubSection(subCommand.getTitle(), subCommand.getContent());

				addImagesToSubSection(subCommand.getImageIdList(), subSection);
				post.addSubSection(subSection);
			}
		}

		post.updateTime();
		return post;
	}

	public RidingPost createRidingPost(User leader, RidingSaveCommand command) {
		var createdPost = toPost(leader, command);

		//썸네일 이미지
		if (!Objects.isNull(command.getThumbnailId())) {
			imageManager.store(command.getThumbnailId(), createdPost);
		}

		//post-subsection mapping
		if (!CollectionUtils.isEmpty(command.getSubCommand()) && command.getSubCommand().size() <= 5) {
			for (RidingSubSaveCommand subCommand : command.getSubCommand()) {
				createdPost.addSubSection(createSubSection(subCommand));
			}
		}
		return createdPost;
	}

	private RidingPost toPost(User leader, RidingSaveCommand command) {
		var mainSection = command.getMainCommand().toSection();

		//address code 검증
		var addressCode = addressCodeRepository.findByCode(mainSection.getAddressCode().getCode())
			.orElseThrow(IllegalArgumentException::new);
		mainSection.assignAddressCode(addressCode);

		//participant section
		var participantSection = command.getParticipantCommand().toSection();

		var post = RidingPost.builder()
			.leader(leader)
			.ridingMainSection(mainSection)
			.ridingParticipantSection(participantSection)
			.build();

		//condition section
		var conditionSection = command.getConditionCommand().toSection();
		post.assignConditionSection(conditionSection);

		//bicycle mapping
		var bicycleList = command.getConditionCommand().getBicycleTypes();
		if (!CollectionUtils.isEmpty(bicycleList)) {
			for (String bicycleName : bicycleList) {
				var bicycle = bicycleRepository.findByName(bicycleName)
					.orElseThrow(IllegalArgumentException::new);
				conditionSection.addBicycle(post, bicycle);
			}
		} else {
			Bicycle bicycle = bicycleRepository.findByName(Bicycle.BicycleName.ALL)
				.orElseThrow(IllegalArgumentException::new);
			conditionSection.addBicycle(post, bicycle);
		}
		return post;
	}

	private RidingSubSection createSubSection(RidingSubSaveCommand command) {
		var section = new RidingSubSection(command.getTitle(), command.getContent());

		//section - image mapping
		if (!CollectionUtils.isEmpty(command.getImageIdList()) && command.getImageIdList().size() <= 2) {
			imageManager.store(command.getImageIdList(), section);
		}
		return section;
	}

	private void addImagesToSubSection(List<Long> imageIdList, RidingSubSection subSection) {
		if (!CollectionUtils.isEmpty(imageIdList) && imageIdList.size() <= 2) {
			//image check
			for (Long id : imageIdList) {
				var findImage = imageRepository.findById(id);
				if (findImage.isPresent()) { //기존 저정되어 있던 image
					subSection.addImage(findImage.get());
				} else {                    //새로운 image
					imageManager.store(id, subSection);
				}
			}
		}
	}
}
