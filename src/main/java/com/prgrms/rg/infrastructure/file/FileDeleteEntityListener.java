package com.prgrms.rg.infrastructure.file;

import javax.persistence.PreRemove;

import org.springframework.beans.factory.annotation.Autowired;

import com.prgrms.rg.domain.common.file.application.FileStore;
import com.prgrms.rg.domain.common.file.model.StoredFile;

public class FileDeleteEntityListener {

	@Autowired
	private FileStore fileStore;

	@PreRemove
	public void removeOriginalFile(StoredFile file) {
		fileStore.delete(file.getUrl());
	}
}
