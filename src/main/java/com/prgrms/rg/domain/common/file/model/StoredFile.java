package com.prgrms.rg.domain.common.file.model;

import static javax.persistence.GenerationType.*;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.prgrms.rg.infrastructure.file.FileDeleteEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EntityListeners(FileDeleteEntityListener.class)
@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class StoredFile {

	public StoredFile(Long id) {
		this.id = id;
	}

	public StoredFile(String originalFileName, String url) {
		this.originalFileName = originalFileName;
		this.url = url;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	protected String originalFileName;
	protected String url;
}
