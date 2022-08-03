package com.prgrms.rg.domain.common.file.application;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.rg.domain.common.file.model.ImageRepository;
import com.prgrms.rg.domain.common.file.model.StoredImage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MultipartImageManager implements ImageManager {

	private final FileStore fileStore;
	private final ImageRepository imageRepository;

	private static final String[] SUPPORTING_EXTENSIONS = {
		"png", "jpg"
	};

	@Override
	@Transactional
	public StoredImage store(MultipartFile multipartFile) {
		if (isFileNotPresent(multipartFile)) {
			throw new EmptyFileException();
		}

		String originalFilename = multipartFile.getOriginalFilename();
		String storedFileName = generateFileNameOf(originalFilename);

		String fileUrl = fileStore.save(multipartFile, storedFileName);
		StoredImage savedImage = imageRepository.save(new StoredImage(originalFilename, fileUrl));

		return savedImage;
	}

	@Override
	@Transactional
	public void delete(Long imageId) {
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
