package com.prgrms.rg.web.user.requests;

import java.util.List;

import lombok.Data;

@Data
public class RidingLeaderEvaluateRequest {

	private Long postId;

	private List<EvaluatedFromLeaderRequest> evaluatedMemberList;

}
