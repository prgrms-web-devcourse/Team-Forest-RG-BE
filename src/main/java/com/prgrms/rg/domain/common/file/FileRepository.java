package com.prgrms.rg.domain.common.file;

public interface FileRepository {
	StoredFile save(StoredFile storedFile);

	void delete(StoredFile file);
}
