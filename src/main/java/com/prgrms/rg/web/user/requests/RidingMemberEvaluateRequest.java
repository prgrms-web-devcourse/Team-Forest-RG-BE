package com.prgrms.rg.web.user.requests;

import java.util.List;

import lombok.Data;

@Data
public class RidingMemberEvaluateRequest {
	private Long postId;

	private List<EvaluatedFromMemberRequest> evaluatedMemberList;

}
