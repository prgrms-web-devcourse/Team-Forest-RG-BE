package com.prgrms.rg.domain.notification.model;

public class NotificationSendFailException extends RuntimeException{
	public NotificationSendFailException(String message) {
		super(message);
	}
}
