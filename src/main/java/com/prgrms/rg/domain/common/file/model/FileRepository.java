package com.prgrms.rg.domain.common.file.model;

import com.prgrms.rg.domain.common.file.model.StoredFile;

public interface FileRepository {
	StoredFile save(StoredFile storedFile);

	void delete(StoredFile file);
}
