package com.prgrms.rg.domain.common.file.model;

public interface ImageRepository {
	StoredImage save(StoredImage image);

	void delete(StoredImage image);

	void deleteById(Long imageId);
}
