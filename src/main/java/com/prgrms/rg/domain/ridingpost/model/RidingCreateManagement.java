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

	public RidingPost createRidingPost(User leader, RidingCreateCommand command) {

		var mainSection = command.getMainCommand().toSection();
		var participantSection = command.getParticipantCommand().toSection();

		var post = RidingPost.builder()
			.leader(leader)
			.ridingMainSection(mainSection)
			.ridingParticipantSection(participantSection)
			.build();

		if (!Objects.isNull(command.getThumbnailId())) {
			imageManager.store(command.getThumbnailId(), post);
		}

		command.getConditionCommand().toSection(post);
		if (!CollectionUtils.isEmpty(command.getSubCommand())) {
			for (RidingSubCreateCommand subCommand : command.getSubCommand()) {
				post.addSubSection(createSubSection(subCommand));
			}
		}
		return post;
	}

	private RidingSubSection createSubSection(RidingSubCreateCommand command) {
		var section = new RidingSubSection(command.getTitle(), command.getContent());

		if (!CollectionUtils.isEmpty(command.getImageIdList()) && command.getImageIdList().size() <= 5) {
			imageManager.store(command.getImageIdList(), section);
		}
		return section;
	}
}
