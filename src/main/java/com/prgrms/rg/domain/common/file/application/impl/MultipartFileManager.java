package com.prgrms.rg.domain.common.file.application.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.rg.domain.common.file.application.FileManager;
import com.prgrms.rg.domain.common.file.application.FileStore;
import com.prgrms.rg.domain.common.file.application.IllegalFileExtensionException;
import com.prgrms.rg.domain.common.file.model.FileRepository;
import com.prgrms.rg.domain.common.file.model.ImageAttachable;
import com.prgrms.rg.domain.common.file.model.StoredFile;

import lombok.RequiredArgsConstructor;

@Transactional
@Component
@RequiredArgsConstructor
public class MultipartFileManager implements FileManager {

	private final FileStore fileStore;
	private final FileRepository fileRepository;

	private static final String[] SUPPORTING_EXTENSIONS = {
		"png", "jpg"
	};

	@Override
	public <T extends ImageAttachable> List<StoredFile> store(List<MultipartFile> multipartFiles, T attached) {
		if (isFilesNotPresent(multipartFiles)) {
			return Collections.emptyList();
		}

		//TODO: 파일 최대 첨부 가능 수에 기반하여 ArrayList의 크기 설정하기
		List<StoredFile> storedFiles = new ArrayList<>();
		int savedCnt = 0;
		for (MultipartFile multipartFile : multipartFiles) {
			Optional<StoredFile> storedFile = store(multipartFile, attached);
			if (storedFile.isPresent()) {
				savedCnt++;
				storedFiles.add(storedFile.get());
			}
		}
		return (savedCnt != 0) ? storedFiles : Collections.emptyList();
	}

	@Override
	public <T extends ImageAttachable> Optional<StoredFile> store(MultipartFile multipartFile, T attached) {
		if (isFileNotPresent(multipartFile)) {
			return Optional.empty();
		}

		String originalFilename = multipartFile.getOriginalFilename();
		String storedFileName = generateFileNameOf(originalFilename);

		String fileUrl = fileStore.save(multipartFile, storedFileName);
		StoredFile savedFile = fileRepository.save(attached.attach(originalFilename, fileUrl));

		return Optional.of(savedFile);
	}

	@Override
	public <T1 extends StoredFile, T2 extends ImageAttachable> void delete(List<T1> files, T2 attached) {
		if (isFilesNotPresent(files)) {
			return;
		}
		for (T1 file : files) {
			fileRepository.delete(file);
		}
		attached.removeCurrentImage();
	}

	private boolean isFilesNotPresent(List files) {
		return files == null || files.isEmpty();
	}

	private boolean isFileNotPresent(MultipartFile multipartFile) {
		return multipartFile == null || multipartFile.isEmpty();
	}

	@Override
	public <T1 extends StoredFile, T2 extends ImageAttachable> void delete(T1 file, T2 attached) {
		if (file == null) {
			return;
		}
		attached.removeCurrentImage();
		fileRepository.delete(file);
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
