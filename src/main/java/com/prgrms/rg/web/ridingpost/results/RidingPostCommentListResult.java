package com.prgrms.rg.web.ridingpost.results;

import java.util.List;

import com.prgrms.rg.domain.ridingpost.application.information.RidingPostCommentInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "from")
public class RidingPostCommentListResult {
	private final List<RidingPostCommentInfo> comments;

}
