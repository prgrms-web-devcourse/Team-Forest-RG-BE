package com.prgrms.rg.web.common.results;

import lombok.Data;

@Data
public class GlobalServerErrorResult {
	public static final GlobalServerErrorResult INTERNAL_SERVER_ERROR = new GlobalServerErrorResult("Internal Server Error");
	public static final GlobalServerErrorResult NOT_FOUND = new GlobalServerErrorResult("Not Found");
	private final String message;

	private GlobalServerErrorResult(String message) {
		this.message = message;
	}

	public static GlobalServerErrorResult of(String message) {
		return new GlobalServerErrorResult(message);
	}

}
