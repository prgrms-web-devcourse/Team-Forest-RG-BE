package com.prgrms.rg.web.common.message;

import javax.servlet.http.HttpServletRequest;

public interface ExceptionMessageSender {
	void send(Exception exception, HttpServletRequest request);

	void send(String message, HttpServletRequest request);
}
