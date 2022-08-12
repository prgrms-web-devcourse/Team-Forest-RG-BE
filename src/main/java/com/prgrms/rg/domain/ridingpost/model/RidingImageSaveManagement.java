package com.prgrms.rg.domain.ridingpost.model;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.prgrms.rg.domain.common.file.application.ImageAttachManager;
import com.prgrms.rg.domain.common.file.model.AttachedImageRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RidingImageSaveManagement {

	private final AttachedImageRepository imageRepository;
	private final ImageAttachManager imageManager;

	//thumbnail save
	public void saveThumbnailImage(RidingPost post, Long thumbnailId) {
		//thumbnail -> 비교
		if (!post.equalToThumbnail(thumbnailId)) {
			post.removeCurrentImage();
			//null이 아닌 썸네일 삽입
			if (!Objects.isNull(thumbnailId)) {
				imageManager.store(thumbnailId, post);
			}
		}
	}

	public void saveSubImages(List<Long> imageIdList, RidingSubSection subSection) {
		imageManager.store(imageIdList, subSection);
	}

	public void updateSubImages(List<Long> imageIdList, RidingSubSection subSection) {
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
