package com.prgrms.rg.domain.common.file.application.impl;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.rg.domain.common.file.application.AjaxImageManager;
import com.prgrms.rg.domain.common.file.application.FileStore;
import com.prgrms.rg.domain.common.file.application.exception.EmptyFileException;
import com.prgrms.rg.domain.common.file.application.exception.IllegalFileExtensionException;
import com.prgrms.rg.domain.common.file.application.exception.IllegalImageIdException;
import com.prgrms.rg.domain.common.file.model.TemporaryImage;
import com.prgrms.rg.domain.common.file.model.TemporaryImageRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@Component
@RequiredArgsConstructor
public class AjaxMultipartImageFileManager implements AjaxImageManager {

	private final FileStore fileStore;
	private final TemporaryImageRepository imageRepository;

	private static final String[] SUPPORTING_EXTENSIONS = {
		"png", "jpg"
	};

	@Override
	public TemporaryImage store(MultipartFile multipartFile) {
		if (isFileNotPresent(multipartFile)) {
			throw new EmptyFileException();
		}

		String originalFilename = multipartFile.getOriginalFilename();
		String storedFileName = generateFileNameOf(originalFilename);

		String fileUrl = fileStore.save(multipartFile, storedFileName);
		return imageRepository.save(new TemporaryImage(originalFilename, fileUrl));
	}

	@Override
	public void delete(Long imageId) {
		TemporaryImage savedImage = imageRepository.findById(imageId)
			.orElseThrow(() -> new IllegalImageIdException(imageId));
		fileStore.delete(savedImage.getUrl());
		imageRepository.deleteById(imageId);
	}

	private boolean isFileNotPresent(MultipartFile multipartFile) {
		return multipartFile == null || multipartFile.isEmpty();
	}

	private String generateFileNameOf(String originalFilename) {
		String uuid = UUID.randomUUID().toString();
		String extension = extractExtensionFrom(originalFilename);
		return uuid + "." + extension;
	}

	private String extractExtensionFrom(String originalFilename) {
		int location = originalFilename.lastIndexOf(".");
		String extension = originalFilename.substring(location + 1);
		if (isSupportedExtension(extension)) {
			return extension;
		}
		throw new IllegalFileExtensionException(extension);
	}

	private boolean isSupportedExtension(String extension) {
		for (String imageExtension : SUPPORTING_EXTENSIONS) {
			if (extension.equalsIgnoreCase(imageExtension)) {
				return true;
			}
		}
		return false;
	}
}
