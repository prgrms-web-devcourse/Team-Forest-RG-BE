package com.prgrms.rg.web.user.api;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.auth.jwt.JwtAuthentication;
import com.prgrms.rg.domain.user.application.UserEvaluateService;
import com.prgrms.rg.web.user.requests.ParticipantEvaluateRequest;
import com.prgrms.rg.web.user.requests.RidingEvaluateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserEvaluateController {
	private final UserEvaluateService evaluateService;

	@Secured("ROLE_USER")
	@PostMapping(value = "/api/v1/user/evaluate")
	public ResponseEntity<String> evaluate(@AuthenticationPrincipal JwtAuthentication auth,
		RidingEvaluateRequest request) {
		evaluateService.evaluateMembers(auth.userId, request.getPostId(),
			request.getEvaluatedMemberList().stream().map(
				ParticipantEvaluateRequest::toCommand).collect(
				Collectors.toList()));
		return ResponseEntity.ok("evaluate 성공");
	}
}
