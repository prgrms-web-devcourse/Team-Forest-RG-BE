package com.prgrms.rg.domain.common.file;

import javax.persistence.PreRemove;

import org.springframework.beans.factory.annotation.Autowired;

public class FileDeleteEntityListener {

	@Autowired
	private FileStore fileStore;

	@PreRemove
	public void removeOriginalFile(StoredFile file) {
		fileStore.delete(file.getUrl());
	}
}
