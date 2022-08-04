package com.prgrms.rg.web.file.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.rg.domain.common.file.application.AsyncImageManager;
import com.prgrms.rg.domain.common.file.model.TemporaryImage;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ImageRestControllerV1 implements ImageRestController{

	private final AsyncImageManager imageManager;

	@PostMapping("/api/v1/images")
	public TemporaryImage storeImage(MultipartFile image) {
		return imageManager.store(image);
	}

	@DeleteMapping("/api/v1/images/{imageId}")
	public void deleteImages(@PathVariable("imageId") Long imageId) {
		imageManager.delete(imageId);
	}
}
