package com.prgrms.rg.domain.ridingpost.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

	@Column(updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	//글 수정 시 서비스 단에서 호출
	public void updateTime() {
		this.updatedAt = LocalDateTime.now();
	}
}
