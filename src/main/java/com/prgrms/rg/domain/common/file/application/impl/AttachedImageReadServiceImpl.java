package com.prgrms.rg.domain.common.file.application.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.prgrms.rg.domain.common.file.application.AttachedImageReadService;
import com.prgrms.rg.domain.common.file.model.AttachedImage;
import com.prgrms.rg.domain.common.file.model.AttachedImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachedImageReadServiceImpl implements AttachedImageReadService {
	//todo repository만 사용?

	private final AttachedImageRepository imageRepository;

	@Override
	public Optional<AttachedImage> getAttachedImageById(Long imageId) {
		return imageRepository.findById(imageId);
	}

}
