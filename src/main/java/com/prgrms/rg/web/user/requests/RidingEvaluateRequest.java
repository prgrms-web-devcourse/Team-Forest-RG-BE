package com.prgrms.rg.web.user.requests;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RidingEvaluateRequest {

	private Long postId;

	private List<ParticipantEvaluateRequest> evaluatedMemberList;

}
