package com.prgrms.rg.domain.common.file.application.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.common.file.application.ImageAttachManger;
import com.prgrms.rg.domain.common.file.application.exception.IllegalImageIdException;
import com.prgrms.rg.domain.common.file.model.AttachedImage;
import com.prgrms.rg.domain.common.file.model.AttachedImageRepository;
import com.prgrms.rg.domain.common.file.model.ImageOwner;
import com.prgrms.rg.domain.common.file.model.TemporaryImage;
import com.prgrms.rg.domain.common.file.model.TemporaryImageRepository;

import lombok.RequiredArgsConstructor;

/**
 * 저장된 이미지 객체 id를 받아서 owner와 JPA연관관계를 갖는 이미지 객체의 생성 및 삭제를 담당합니다.
 */
@Transactional
@Component
@RequiredArgsConstructor
public class ImageAttachManagerImpl implements ImageAttachManger {

	private final TemporaryImageRepository temporaryImageRepository;
	private final AttachedImageRepository attachedImageRepository;

	@Override
	public <T extends ImageOwner> List<AttachedImage> store(List<Long> temporaryImageIds, T owner) {
		List<AttachedImage> images = new ArrayList<>();
		for (Long temporaryImageId : temporaryImageIds) {
			images.add(store(temporaryImageId, owner));
		}
		return images;
	}

	@Override
	public <T extends ImageOwner> AttachedImage store(Long temporaryImageId, T owner) {
		TemporaryImage storedImage = temporaryImageRepository.findById(temporaryImageId)
			.orElseThrow(() -> new IllegalImageIdException(temporaryImageId));

		AttachedImage attachedImage = attachedImageRepository.save(
			owner.attach(storedImage.getOriginalFileName(), storedImage.getUrl()));
		temporaryImageRepository.deleteById(temporaryImageId);

		return attachedImage;
	}

	@Override
	public <T1 extends AttachedImage, T2 extends ImageOwner> void delete(List<T1> images, T2 attached) {
		if (isImagesNotPresent(images)) {
			return;
		}
		attachedImageRepository.deleteAll(images);
		attached.removeCurrentImage();
	}

	private boolean isImagesNotPresent(List files) {
		return files == null || files.isEmpty();
	}

	@Override
	public <T1 extends AttachedImage, T2 extends ImageOwner> void delete(T1 image, T2 attached) {
		if (image == null) {
			return;
		}
		attached.removeCurrentImage();
		attachedImageRepository.delete(image);
	}
}
