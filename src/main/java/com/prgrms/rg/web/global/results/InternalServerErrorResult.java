package com.prgrms.rg.web.global.results;

import lombok.Data;

@Data
public class InternalServerErrorResult {
	private static final String DEFAULT_MESSAGE = "Internal Server Error";
	private final String message;

	private InternalServerErrorResult(String message) {
		this.message = message;
	}

	public static InternalServerErrorResult create() {
		return new InternalServerErrorResult(DEFAULT_MESSAGE);
	}

}
