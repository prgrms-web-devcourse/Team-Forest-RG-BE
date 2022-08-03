package com.prgrms.rg.domain.common.file.model;

public interface AttachedImageRepository {
	AttachedImage save(AttachedImage storedFile);

	void delete(AttachedImage file);

	void deleteAll(Iterable<? extends AttachedImage> entities);
}
