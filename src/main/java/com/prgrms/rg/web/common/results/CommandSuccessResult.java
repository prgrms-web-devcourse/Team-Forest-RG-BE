package com.prgrms.rg.web.common.results;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Command 성공 공통 응답 DTO<br/>
 * 적용 대상 엔티티의 ID 값만 반환합니다.
 */
@Data
@RequiredArgsConstructor(staticName = "from")
public class CommandSuccessResult {
	private final long id;

}
