package com.prgrms.rg.infrastructure.file;

import javax.persistence.PreRemove;

import org.springframework.beans.factory.annotation.Autowired;

import com.prgrms.rg.domain.common.file.application.FileStore;
import com.prgrms.rg.domain.common.file.model.AttachedImage;

public class ImageDeleteEntityListener {

	@Autowired
	private FileStore fileStore;

	@PreRemove
	public void removeOriginalFile(AttachedImage file) {
		// fileStore.delete(file.getUrl());
	}
}
