package com.prgrms.rg.domain.common.file.model;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.prgrms.rg.infrastructure.file.ImageDeleteEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class TemporaryImage {

	public TemporaryImage(String originalFileName, String url) {
		this.originalFileName = originalFileName;
		this.url = url;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	protected String originalFileName;
	protected String url;
}
