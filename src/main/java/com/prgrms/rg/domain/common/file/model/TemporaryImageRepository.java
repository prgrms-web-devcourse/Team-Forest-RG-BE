package com.prgrms.rg.domain.common.file.model;

import java.util.Optional;

public interface TemporaryImageRepository {
	TemporaryImage save(TemporaryImage image);

	void delete(TemporaryImage image);

	void deleteById(Long imageId);

	Optional<TemporaryImage> findById(Long originalImageId);
}
