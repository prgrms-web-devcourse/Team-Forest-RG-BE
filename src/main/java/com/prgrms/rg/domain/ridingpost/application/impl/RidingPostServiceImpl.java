package com.prgrms.rg.domain.ridingpost.application.impl;

import static com.google.common.base.Preconditions.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.common.event.DomainEventPublisher;
import com.prgrms.rg.domain.ridingpost.application.RidingPostService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSaveCommand;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;
import com.prgrms.rg.domain.ridingpost.model.RidingSaveManagement;
import com.prgrms.rg.domain.ridingpost.model.event.RidingPostDeleteEvent;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingPostNotFoundException;
import com.prgrms.rg.domain.ridingpost.model.exception.UnAuthorizedException;
import com.prgrms.rg.domain.ridingpost.model.image.RidingImageSaveManagement;
import com.prgrms.rg.domain.user.application.UserReadService;
import com.prgrms.rg.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RidingPostServiceImpl implements RidingPostService {

	private final UserReadService userReadService;
	private final RidingPostRepository ridingPostRepository;
	private final RidingSaveManagement saveManagement;
	private final RidingImageSaveManagement imageSaveManagement;
	private final DomainEventPublisher eventPublisher;

	@Transactional
	public Long createRidingPost(Long userId, RidingSaveCommand command) {
		User user = userReadService.getUserEntityById(userId);

		//post, subsection 저장
		RidingPost savedPost = ridingPostRepository.save(saveManagement.createRidingPost(user, command));

		//image save
		imageSaveManagement.saveThumbnailImage(savedPost, command.getThumbnailId());
		for (int i = 0; i < savedPost.getSubSectionList().size(); i++) {
			var subSection = savedPost.getSubSectionList().get(i);
			imageSaveManagement.saveSubImages(command.getSubCommand().get(i).getImageIdList(), subSection);
		}

		//saved의 id return
		return savedPost.getId();
	}

	@Override
	@Transactional
	public Long updateRidingPost(Long leaderId, Long postId, RidingSaveCommand command) {

		var post = checkAndFindPost(leaderId, postId);
		var updatedPost = saveManagement.updateRidingPost(post.getLeader(), post, command);

		// image
		imageSaveManagement.saveThumbnailImage(updatedPost, command.getThumbnailId());
		for (int i = 0; i < updatedPost.getSubSectionList().size(); i++) {
			var subSection = updatedPost.getSubSectionList().get(i);
			imageSaveManagement.updateSubImages(command.getSubCommand().get(i).getImageIdList(), subSection);
		}

		return post.getId();
	}

	@Override
	@Transactional
	public void deleteRidingPost(Long leaderId, Long postId) {
		checkAndFindPost(leaderId, postId);

		ridingPostRepository.deleteById(postId);

		RidingPostDeleteEvent deleteEvent = new RidingPostDeleteEvent(postId);
		eventPublisher.publish(deleteEvent);
	}

	private RidingPost checkAndFindPost(Long leaderId, Long postId) {
		var post = ridingPostRepository.findById(postId).orElseThrow(() -> new RidingPostNotFoundException(postId));
		var leader = post.getLeader();
		checkArgument(leader.getId().equals(leaderId), new UnAuthorizedException(leaderId));
		return post;
	}

}
