package com.prgrms.rg.domain.common.model;

import javax.persistence.Entity;

import com.prgrms.rg.domain.common.file.model.AttachedImage;

/*
 * JpaFileRepository 로드 통과용 임시 클래스
 * StoredFile을 상속받은 클래스가 나오면 삭제해야 합니다.
 */
@Entity
public class DummyStoredFile extends AttachedImage {
}
