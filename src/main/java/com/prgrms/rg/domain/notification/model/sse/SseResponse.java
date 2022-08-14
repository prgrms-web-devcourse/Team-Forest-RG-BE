package com.prgrms.rg.domain.notification.model.sse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class SseResponse {
	private String message;

	public static SseResponse createConnectSuccess(Long connectorId) {
		SseResponse instance = new SseResponse();
		instance.setMessage("EventStream Created. [userId= " + connectorId + "]");
		return instance;
	}
}
