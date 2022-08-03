package com.prgrms.rg.domain.ridingpost.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.prgrms.rg.domain.ridingpost.application.command.RidingCreateCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSubCreateCommand;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingSubSection;
import com.prgrms.rg.domain.ridingpost.model.RidingThumbnailImage;
import com.prgrms.rg.domain.ridingpost.model.SubImage;
import com.prgrms.rg.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RidingCreateManagement {

	public RidingPost createRidingPost(User leader, RidingCreateCommand command) {

		var mainSection = command.getMainCommand().toSection();
		var participantSection = command.getParticipantCommand().toSection();

		var post = RidingPost.builder()
			.leader(leader)
			.ridingMainSection(mainSection)
			.ridingParticipantSection(participantSection)
			.build();

		//todo image
		new RidingThumbnailImage(command.getThumbnailId(), post);

		//post mapping -> 내부에서
		command.getConditionCommand().toSection(post);
		for (RidingSubCreateCommand subCommand : command.getSubCommand()) {
			post.addSubSection(createSubSection(subCommand));
		}
		return post;
	}

	private RidingSubSection createSubSection(RidingSubCreateCommand command) {
		var section = new RidingSubSection(command.getTitle(), command.getContent());

		//todo image
		List<SubImage> imageList = new ArrayList<>();
		for (Long imageId : command.getImageIdList()) {
			imageList.add(new SubImage(imageId, section));
		}
		return section;
	}
}
