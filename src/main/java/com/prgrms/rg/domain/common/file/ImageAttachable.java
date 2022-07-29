package com.prgrms.rg.domain.common.file;

/**
 * 이미지를 붙일 수 있는 타입입니다.
 * attach(String fileName, String fileUrl)메서드로 이미지 객체와 연관관계를 맺어주며,
 * 해당 메서드의 호출은 FileManager가 수행합니다.
 */
public interface ImageAttachable {
	StoredFile attach(String fileName, String fileUrl);

	/**
	 * 새로운 사진 엔티티를 설정하기 위해서 기존 사진을 지우는 연관관계 편의 메서드입니다.
	 * @ImplSpec 이미지가 하나인 경우 null, Optional.empty()등으로 이미지가 할당되기 전의 상태로 설정해주고,
	 * 이미지가 여럿인 경우 빈 List로 이미지 엔티티 필드를 초기화해주면 됩니다.
	 */
	void removeCurrentImage();
}
