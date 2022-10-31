package com.prgrms.rg.domain.ridingpost.application.impl;

import static com.prgrms.rg.testutil.TestEntityDataFactory.createRidingPost;
import static com.prgrms.rg.testutil.TestEntityDataFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;

import com.prgrms.rg.domain.common.RelatedEntityNotFoundException;
import com.prgrms.rg.domain.ridingpost.application.RidingPostCommentService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingPostCommentCreateCommand;
import com.prgrms.rg.domain.ridingpost.model.RidingPostComment;
import com.prgrms.rg.domain.ridingpost.model.RidingPostCommentRepository;
import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;
import com.prgrms.rg.domain.user.model.Manner;
import com.prgrms.rg.domain.user.model.Nickname;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:address_code.sql", "classpath:bicycle.sql"})
class RidingPostCommentServiceImplTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  RidingPostRepository ridingPostRepository;

  @Autowired
  RidingPostCommentService ridingPostCommentService;

  @Autowired
  RidingPostCommentRepository ridingPostCommentRepository;

  @PersistenceContext
  EntityManager entityManager;

  @Test
  @DisplayName("요청을 받아 사용자와 라이딩 게시물 정보와 연결된 댓글을 저장한다. - 부모 댓글")
  void create_parent_comment() {
    // Given
    var commentAuthor = createUser();
    var leader = User.builder().nickname(new Nickname("leader")).manner(Manner.create()).build();
    userRepository.save(commentAuthor);
    userRepository.save(leader);
    var post = createRidingPost(leader.getId());
    post = ridingPostRepository.save(post);
    var command = RidingPostCommentCreateCommand.of(commentAuthor.getId(), post.getId(), null, "comment");

    // When
    var commentId = ridingPostCommentService.createComment(command);

    // Then
    var savedComment = ridingPostCommentRepository.findById(commentId);
    assertThat(savedComment).isNotNull();
    assertThat(savedComment.getContents()).isEqualTo("comment");
    assertThat(savedComment.getAuthor()).isEqualTo(commentAuthor);
    assertThat(savedComment.getRidingPost()).isEqualTo(post);
    assertThat(savedComment.getParentComment()).isNull();

  }

  @Test
  @DisplayName("요청을 받아 사용자와 라이딩 게시물 정보와 연결된 댓글을 저장한다. - 자식 댓글")
  void create_child_comment() {
    // Given
    var commentAuthor = createUser();
    var leader = User.builder().nickname(new Nickname("leader")).manner(Manner.create()).build();
    userRepository.save(commentAuthor);
    userRepository.save(leader);
    var post = createRidingPost(leader.getId());
    post = ridingPostRepository.save(post);
    var parentComment = RidingPostComment.createRootComment(leader, post, "parent");
    parentComment = ridingPostCommentRepository.save(parentComment);
    var command = RidingPostCommentCreateCommand.of(commentAuthor.getId(), post.getId(), parentComment.getId(), "comment");

    // When
    var commentId = ridingPostCommentService.createComment(command);

    // Then
    var savedComment = ridingPostCommentRepository.findById(commentId);
    assertThat(savedComment).isNotNull();
    assertThat(savedComment.getContents()).isEqualTo("comment");
    assertThat(savedComment.getAuthor()).isEqualTo(commentAuthor);
    assertThat(savedComment.getRidingPost()).isNull();

    RidingPostComment savedParentComment = savedComment.getParentComment();
    assertThat(savedParentComment).isEqualTo(parentComment);
    assertThat(savedParentComment.getChildComments()).contains(savedComment);

  }

  @Test
  @DisplayName("연관된 엔티티들을 가져오지 못했을 경우 엔티티 찾기 실패 예외를 던진다.")
  void throws_entity_not_found_exception_when_querying_related_entity_failed() {

    // Given
    var invalidUserId = 2L;

    // When
    ThrowingCallable when =
        () -> ridingPostCommentService.createComment(RidingPostCommentCreateCommand.of(invalidUserId, 1L, null, "wrong"));

    // Then
    assertThatThrownBy(when)
        .isInstanceOf(RelatedEntityNotFoundException.class);

  }

  @Test
  @DisplayName("특정 RidingPost의 댓글들을 조회해서 생성 시간 기준 오름차순으로 반환한다.")
  void query_comments_related_with_specific_riding_post() {

    // Given
    var commentAuthor = createUser();
    var leader = User.builder().nickname(new Nickname("leader")).manner(Manner.create()).build();
    userRepository.save(commentAuthor);
    userRepository.save(leader);
    var post = createRidingPost(leader.getId());
    post = ridingPostRepository.save(post);

    for (int i = 0; i < 100; i++) {
      var rootComment = RidingPostComment.createRootComment(commentAuthor, post, "parent");
      rootComment = ridingPostCommentRepository.save(rootComment);
      var childCommentId = ridingPostCommentService.createComment(
          RidingPostCommentCreateCommand.of(leader.getId(), post.getId(), rootComment.getId(), "child"));
      var secondChildCommentId = ridingPostCommentService.createComment(
          RidingPostCommentCreateCommand.of(leader.getId(), post.getId(), rootComment.getId(), "child2"));
    }

    entityManager.flush();
    entityManager.clear();

    // When
    var commentsInfo = ridingPostCommentService.getCommentsByPostId(post.getId());

    // Then
    assertThat(commentsInfo).hasSize(100);
    var rootCommentInfo = commentsInfo.get(0);
    assertThat(rootCommentInfo.getAuthorName()).isEqualTo(commentAuthor.getNickname());
    assertThat(rootCommentInfo.getChildComments()).hasSize(2);
    assertThat(rootCommentInfo.getParentCommentId()).isZero();
    var childCommentInfo = rootCommentInfo.getChildComments().get(0);
    assertThat(childCommentInfo.getContents()).isEqualTo("child");
    var secondChildCommentInfo = rootCommentInfo.getChildComments().get(1);
    assertThat(secondChildCommentInfo.getContents()).isEqualTo("child2");

  }

  @Test
  @DisplayName("권한을 가진 사용자의 댓글 내용 수정 요청을 수행한다.")
  void handle_update_command_with_proper_user() {

    // Given
    var commentAuthor = createUser();
    var leader = User.builder().nickname(new Nickname("leader")).manner(Manner.create()).build();
    userRepository.save(commentAuthor);
    userRepository.save(leader);

    var post = createRidingPost(leader.getId());
    post = ridingPostRepository.save(post);

    var comment = RidingPostComment.createRootComment(commentAuthor, post, "parent");
    comment = ridingPostCommentRepository.save(comment);

    // When
    ridingPostCommentService.updateComment(commentAuthor.getId(), comment.getId(), "changed");

    // Then
    assertThat(comment.getContents()).isEqualTo("changed");

  }

  @Test
  @DisplayName("권한을 가진 사용자의 댓글 내용 삭제 요청을 수행한다.")
  void handle_delete_command_with_authorized_user() {

    // Given
    var commentAuthor = createUser();
    var leader = User.builder().nickname(new Nickname("leader")).manner(Manner.create()).build();
    userRepository.save(commentAuthor);
    userRepository.save(leader);

    var post = createRidingPost(leader.getId());
    post = ridingPostRepository.save(post);

    var comment = RidingPostComment.createRootComment(commentAuthor, post, "parent");
    comment = ridingPostCommentRepository.save(comment);
    var commentId = comment.getId();

    // When
    ridingPostCommentService.removeComment(commentAuthor.getId(), commentId);

    // Then
    assertThat(ridingPostCommentRepository.findById(commentId)).isNull();

  }

  @Test
  @DisplayName("권한을 가진 사용자의 댓글 삭제 요청을 받고 자식 댓글들도 삭제한다.")
  void handle_delete_command_with_authorized_user_child() {

    // Given
    var commentAuthor = createUser();
    var leader = User.builder().nickname(new Nickname("leader")).manner(Manner.create()).build();
    userRepository.save(commentAuthor);
    userRepository.save(leader);

    var post = createRidingPost(leader.getId());
    post = ridingPostRepository.save(post);

    var parent = RidingPostComment.createRootComment(commentAuthor, post, "parent");
    parent = ridingPostCommentRepository.save(parent);
    var parentId = parent.getId();

    var child = RidingPostComment.createChildComment(commentAuthor, parent, "child");
    child = ridingPostCommentRepository.save(child);
    var childId = child.getId();

    // When
    ridingPostCommentService.removeComment(commentAuthor.getId(), parentId);

    // Then
    entityManager.flush();
    entityManager.clear();
    assertThat(ridingPostCommentRepository.findById(parentId)).isNull();
    assertThat(ridingPostCommentRepository.findById(childId)).isNull();

  }

}
