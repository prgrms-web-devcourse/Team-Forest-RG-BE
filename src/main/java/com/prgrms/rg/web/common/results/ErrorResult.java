package com.prgrms.rg.web.common.results;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResult {
	private int errorCode;
	private String message;
}
