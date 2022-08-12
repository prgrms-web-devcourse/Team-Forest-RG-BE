package com.prgrms.rg.web.ridingpost.api;

import javax.validation.Valid;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.auth.jwt.JwtAuthentication;
import com.prgrms.rg.domain.ridingpost.application.RidingPostCommentService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingPostCommentCreateCommand;
import com.prgrms.rg.web.common.results.CommandSuccessResult;
import com.prgrms.rg.web.ridingpost.requests.RidingPostCommentCreateRequest;
import com.prgrms.rg.web.ridingpost.requests.RidingPostCommentUpdateRequest;
import com.prgrms.rg.web.ridingpost.results.RidingPostCommentListResult;

@RestController
public class RidingPostCommentController {

	private final RidingPostCommentService commentService;

	public RidingPostCommentController(RidingPostCommentService commentService) {
		this.commentService = commentService;
	}

	@Secured("ROLE_USER")
	@PostMapping("/api/v1/ridingposts/{postid}/comments")
	public CommandSuccessResult createRidingComment(
		@Valid @RequestBody RidingPostCommentCreateRequest request,
		@PathVariable("postid") long postId,
		@AuthenticationPrincipal JwtAuthentication auth) {
		var userId = auth.userId;
		var command = RidingPostCommentCreateCommand.of(userId, postId, request.getParentCommentId(), request.getContents());

		var commentId = commentService.createComment(command);

		return CommandSuccessResult.from(commentId);
	}

	@GetMapping("/api/v1/ridingposts/{postid}/comments")
	public RidingPostCommentListResult getRidingComment(@PathVariable("postid") long postId) {

		return RidingPostCommentListResult.from(commentService.getCommentsByPostId(postId));
	}

	@Secured("ROLE_USER")
	@PutMapping("/api/v1/ridingposts/{postid}/comments/{commentid}")
	public CommandSuccessResult updateRidingComment(
		@Valid @RequestBody RidingPostCommentUpdateRequest command,
		@PathVariable("postid") long postId,
		@PathVariable("commentid") long commentId,
		@AuthenticationPrincipal JwtAuthentication auth) {

		var userId = auth.userId;
		commentService.updateComment(userId, commentId, command.getContents());

		return CommandSuccessResult.from(commentId);
	}

	@Secured("ROLE_USER")
	@DeleteMapping("/api/v1/ridingposts/{postid}/comments/{commentid}")
	public CommandSuccessResult removeRidingComment(
		@PathVariable("postid") long postId,
		@PathVariable("commentid") long commentId,
		@AuthenticationPrincipal JwtAuthentication auth) {

		var userId = auth.userId;
		commentService.removeComment(userId, commentId);

		return CommandSuccessResult.from(commentId);
	}
}
