package com.prgrms.rg.domain.ridingpost.model;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.prgrms.rg.domain.common.file.application.ImageAttachManger;
import com.prgrms.rg.domain.ridingpost.application.command.RidingCreateCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSubCreateCommand;
import com.prgrms.rg.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RidingCreateManagement {

	private final ImageAttachManger imageManager;
	private final AddressCodeRepository addressCodeRepository;
	private final BicycleRepository bicycleRepository;

	public RidingPost createRidingPost(User leader, RidingCreateCommand command) {

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

		//썸네일 이미지
		if (!Objects.isNull(command.getThumbnailId())) {
			imageManager.store(command.getThumbnailId(), post);
		}

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
		}

		//post-subsection mapping
		if (!CollectionUtils.isEmpty(command.getSubCommand())) {
			for (RidingSubCreateCommand subCommand : command.getSubCommand()) {
				post.addSubSection(createSubSection(subCommand));
			}
		}
		return post;
	}

	private RidingSubSection createSubSection(RidingSubCreateCommand command) {
		var section = new RidingSubSection(command.getTitle(), command.getContent());

		//section - image mapping
		if (!CollectionUtils.isEmpty(command.getImageIdList()) && command.getImageIdList().size() <= 5) {
			imageManager.store(command.getImageIdList(), section);
		}
		return section;
	}
}
