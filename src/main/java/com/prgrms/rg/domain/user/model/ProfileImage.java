package com.prgrms.rg.domain.user.model;

import static lombok.AccessLevel.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.prgrms.rg.domain.common.file.model.StoredFile;

import lombok.NoArgsConstructor;

//TODO: Kakao 회원가입과 연동
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ProfileImage extends StoredFile {

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	public ProfileImage(String originalFileName, String url, User user) {
		super(originalFileName, url);
		this.user = user;
	}
}
