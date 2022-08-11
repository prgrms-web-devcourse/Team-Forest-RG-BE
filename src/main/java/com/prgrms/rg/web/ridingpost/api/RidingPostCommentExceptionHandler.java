package com.prgrms.rg.web.ridingpost.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.prgrms.rg.domain.common.RelatedEntityNotFoundException;
import com.prgrms.rg.web.common.RgControllerAdvice;
import com.prgrms.rg.web.common.results.GlobalServerErrorResult;

import lombok.extern.slf4j.Slf4j;

@RgControllerAdvice(assignableTypes = RidingPostCommentController.class)
@Slf4j
public class RidingPostCommentExceptionHandler {

	@ExceptionHandler(RelatedEntityNotFoundException.class)
	public ResponseEntity<GlobalServerErrorResult> handleRelatedEntityNotFoundException(RelatedEntityNotFoundException exception) {
		log.info(exception.getMessage(), exception);

		return ResponseEntity.unprocessableEntity().body(GlobalServerErrorResult.from("필요한 사용자 정보, 라이딩 정보를 불러오는 데 실패했습니다."));
	}
}
