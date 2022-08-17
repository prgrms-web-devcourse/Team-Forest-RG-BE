package com.prgrms.rg.web.ridingpost.requests;

import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class RidingPostCommentCreateRequest {
	@NotEmpty
	private String contents;

	@Nullable
	private Long parentCommentId;
}
