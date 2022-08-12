package com.prgrms.rg.web.user.requests;

import lombok.Data;

@Data
public class EvaluatedMemberRequest {

	private final Long memberId;
	private final boolean recommended;
	private final boolean noshow;


}
