package com.prgrms.rg.web.user.requests;

import java.util.List;

import lombok.Data;

@Data
public class RidingEvaluateRequest {

	private Long postId;

	private List<ParticipantEvaluateRequest> evaluatedMemberList;

}
