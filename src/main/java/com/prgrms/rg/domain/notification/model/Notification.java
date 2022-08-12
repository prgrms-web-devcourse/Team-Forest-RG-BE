package com.prgrms.rg.domain.notification.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private RidingPost ridingPost;

	private boolean isRead = false;
	private String contents;
	private NotificationType type;

	public static Notification createRidingJoinNotification(User user, RidingPost post) {
		Notification instance = new Notification();
		instance.setUser(user);
		instance.setRidingPost(post);
		instance.setContents("new user join your riding");
		instance.setType(NotificationType.RIDING_JOIN);
		return instance;
	}

	public void read() {
		setRead(true);
	}

}
