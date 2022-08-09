package com.prgrms.rg.web.common;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.web.common.results.GlobalServerErrorResult;

/*
 서버에 유효하지 않은 URL 요청을 받았을 때 404 상태 코드와 json을 응답합니다.
 */
@RestController
public class CustomErrorController implements ErrorController {

	@RequestMapping("${server.error.path:/error}")
	public ResponseEntity<GlobalServerErrorResult> sendNotFoundMessage() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GlobalServerErrorResult.NOT_FOUND);
	}
}
