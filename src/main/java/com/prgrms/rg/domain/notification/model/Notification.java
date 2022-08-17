package com.prgrms.rg.domain.notification.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.prgrms.rg.domain.common.model.BaseTimeEntity;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
public class Notification extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "post_id")
	private RidingPost ridingPost;

	private boolean isRead = false;
	private String contents;
	private NotificationType type;

	public static Notification createNotification(User user, RidingPost post, NotificationType type) {
		Notification instance = new Notification();
		instance.setUser(user);
		instance.setRidingPost(post);
		String postTitle = post.getRidingMainSection().getTitle();
		instance.setContents(MessageBuilder.buildMessage(postTitle, type));
		instance.setType(type);
		return instance;
	}

	public void read() {
		setRead(true);
	}

	private static class MessageBuilder {
		public static String buildMessage(String ridingTitle, NotificationType type) {
			switch (type) {
				case RIDING_JOIN:
					return buildRidingJoinMessage(ridingTitle);
				case RIDING_JOIN_CANCEL:
					return buildJoinCancelMessage(ridingTitle);
				case RIDING_DELETE:
					return buildRidingDeleteMessage(ridingTitle);
				default:
					throw new IllegalArgumentException("invalid notification type");
			}
		}

		private static String buildRidingJoinMessage(String ridingTitle) {
			return "'"
				+ ridingTitle
				+ "' "
				+ "라이딩에 새로운 라이더가 참가했어요!";
		}

		private static String buildJoinCancelMessage(String ridingTitle) {
			return "'"
				+ ridingTitle
				+ "' "
				+ "라이딩에 참가 취소자가 생겼어요!";
		}

		private static String buildRidingDeleteMessage(String ridingTitle) {
			return "'"
				+ ridingTitle
				+ "' "
				+ "라이딩이 취소되었어요!";
		}

	}

}
