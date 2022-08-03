package com.prgrms.rg.web.file.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.rg.domain.common.file.application.ImageManager;
import com.prgrms.rg.domain.common.file.model.StoredImage;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ImageRestControllerV1 {

	private final ImageManager imageManager;

	@PostMapping("/api/v1/images")
	public StoredImage storeImage(MultipartFile image) {
		return imageManager.store(image);
	}

	@DeleteMapping("/api/v1/images/{imageId}")
	public void deleteImages(@PathVariable("imageId") Long imageId) {
		imageManager.delete(imageId);
	}
}
