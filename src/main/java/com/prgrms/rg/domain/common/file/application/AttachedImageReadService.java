package com.prgrms.rg.domain.common.file.application;

import java.util.Optional;

import com.prgrms.rg.domain.common.file.model.AttachedImage;

public interface AttachedImageReadService {

	Optional<AttachedImage> getAttachedImageById(Long imageId);

}
