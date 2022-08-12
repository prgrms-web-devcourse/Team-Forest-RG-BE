package com.prgrms.rg.web.user.api;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.auth.jwt.JwtAuthentication;
import com.prgrms.rg.domain.user.application.UserEvaluateService;
import com.prgrms.rg.web.user.requests.EvaluatedFromLeaderRequest;
import com.prgrms.rg.web.user.requests.EvaluatedFromMemberRequest;
import com.prgrms.rg.web.user.requests.RidingLeaderEvaluateRequest;
import com.prgrms.rg.web.user.requests.RidingMemberEvaluateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserEvaluateController {
	private final UserEvaluateService evaluateService;

	@Secured("ROLE_USER")
	@PostMapping(value = "/api/v1/user/evaluate/leader")
	public ResponseEntity<String> evaluate(@AuthenticationPrincipal JwtAuthentication auth,
		RidingLeaderEvaluateRequest request) {
		evaluateService.evaluateFromLeader(auth.userId, request.getPostId(),
			request.getEvaluatedMemberList().stream().map(
				EvaluatedFromLeaderRequest::toCommand).collect(
				Collectors.toList()));
		return ResponseEntity.ok("evaluate 성공");
	}

	@Secured("ROLE_USER")
	@PostMapping(value = "/api/v1/user/evaluate/member")
	public ResponseEntity<String> evaluate(@AuthenticationPrincipal JwtAuthentication auth,
		RidingMemberEvaluateRequest request) {
		evaluateService.evaluateFromMember(auth.userId, request.getPostId(),
			request.getEvaluatedMemberList().stream().map(
				EvaluatedFromMemberRequest::toCommand).collect(Collectors.toList())
		);
		return ResponseEntity.ok("evaluate 성공");
	}
}
