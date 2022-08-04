package com.prgrms.rg.domain.common.file.model;

import java.util.Optional;

public interface AttachedImageRepository {
	AttachedImage save(AttachedImage storedFile);

	void delete(AttachedImage file);

	void deleteAll(Iterable<? extends AttachedImage> entities);

	Optional<AttachedImage> findById(Long originalImageId);

	void deleteById(Long imageId);
}
