package com.prgrms.rg.domain.common.file.application;

import org.springframework.web.multipart.MultipartFile;

import com.prgrms.rg.domain.common.file.model.StoredImage;

public interface ImageManager {

	StoredImage store(MultipartFile multipartFile);

	void delete(Long imageId);
}
