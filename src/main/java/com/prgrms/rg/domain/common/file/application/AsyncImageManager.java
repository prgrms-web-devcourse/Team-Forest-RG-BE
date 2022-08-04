package com.prgrms.rg.domain.common.file.application;

import org.springframework.web.multipart.MultipartFile;

import com.prgrms.rg.domain.common.file.model.TemporaryImage;

/**
 * 프론트엔드 측에서 비동기 방식으로 전달되는 이미지 파일의 저장 및 삭제를 담당합니다.
 * 이 때 생성된 이미지 데이터 엔티티는 이후 JPA 연관관계 설정을 위해 ImageAttachManager에서 복제된 이후 삭제됩니다.
 */
public interface AsyncImageManager {

	TemporaryImage store(MultipartFile multipartFile);

	void delete(Long imageId);
}
