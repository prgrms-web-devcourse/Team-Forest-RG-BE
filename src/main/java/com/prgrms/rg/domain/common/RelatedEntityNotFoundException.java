package com.prgrms.rg.domain.common;

public class RelatedEntityNotFoundException extends RuntimeException {
	public RelatedEntityNotFoundException(Throwable cause) {

		super("연관 엔티티 탐색이 실패했습니다.", cause);
	}
}
