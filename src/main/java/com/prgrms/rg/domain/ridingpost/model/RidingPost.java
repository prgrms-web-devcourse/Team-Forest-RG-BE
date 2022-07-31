package com.prgrms.rg.domain.ridingpost.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import com.prgrms.rg.domain.common.file.ImageAttachable;
import com.prgrms.rg.domain.common.file.StoredFile;
import com.prgrms.rg.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//TODO 제약 조건
@Setter(value = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RidingPost extends BaseTimeEntity implements ImageAttachable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "host_id", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User host;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "contents")
	private String contents;

	@Column(name = "participant_count")
	private int participantCount = 0;

	@Column(name = "max_participant_count")
	private int maxParticipantCount;

	@Column(name = "min_participant_count")
	private int minParticipantCount;

	//TODO string collection
	@ElementCollection
	@CollectionTable(name = "riding_route", joinColumns =
	@JoinColumn(name = "post_id"))
	@OrderColumn(name = "list_idx")
	private List<String> routes;

	@Column(name = "riding_date", nullable = false)
	private LocalDateTime ridingDate;

	//TODO thumbnail, images 병합 -> RidingImage의 isThumbnail field
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<RidingImage> images;

	@Column(name = "fee")
	private Long fee = 0L;

	//condition
	@Enumerated(EnumType.STRING)
	@Column(name = "level")
	private RidingLevel level;

	@Column(name = "riding_year")
	private int ridingYear;

	//TODO link entity?
	@ManyToMany
	@JoinTable(name = "post_bicycle")
	private Set<Bicycle> bicycleList = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY)
	private Set<RidingParticipant> participants = new HashSet<>();


	public static RidingPost createPost(User host, String title, String contents, int maxParticipantCount, int minParticipantCount,
		List<String> routes, LocalDateTime ridingDate,
		Long fee, RidingLevel level, int ridingYear, Set<Bicycle> bicycleList){
		RidingPost post = new RidingPost();
		post.assignHost(host);
		post.setTitle(title);
		post.setContents(contents);
		post.setMaxParticipantCount(maxParticipantCount);
		post.setMinParticipantCount(minParticipantCount);
		post.setRoutes(routes);
		post.setRidingDate(ridingDate);
		post.setFee(fee);
		post.setLevel(level);
		post.setRidingYear(ridingYear);
		post.setBicycleList(bicycleList);
		return post;

	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public void setMaxParticipantCount(int maxParticipantCount) {
		this.maxParticipantCount = maxParticipantCount;
	}

	public void setMinParticipantCount(int minParticipantCount) {
		this.minParticipantCount = minParticipantCount;
	}

	public void setRoutes(List<String> routes) {
		this.routes = routes;
	}

	public void setRidingDate(LocalDateTime ridingDate) {
		this.ridingDate = ridingDate;
	}

	public void setFee(Long fee) {
		this.fee = fee;
	}

	public void setLevel(RidingLevel level) {
		this.level = level;
	}

	public void setRidingYear(int ridingYear) {
		this.ridingYear = ridingYear;
	}

	public void setBicycleList(Set<Bicycle> bicycleList) {
		this.bicycleList = bicycleList;
	}

	//update
	public RidingImage assignThumbnail(RidingImage thumbnail) {
		thumbnail.setThumbnail();
		images.add(thumbnail);
		return thumbnail;
	}

	private void assignHost(User host) {
		this.host = host;
		participants.add(new RidingParticipant(this, host, true));
	}

	public void addParticipant(User participant){
		//TODO 동시성 문제 발생 가능성 있음
		participantCount++;
		participants.add(new RidingParticipant(this, participant, false));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RidingPost that = (RidingPost)o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public List<String> getRoutes() {
		return Collections.unmodifiableList(routes);
	}

	@Override
	public StoredFile attach(String fileName, String fileUrl) {
		RidingImage image = new RidingImage(fileName, fileUrl, this, false);
		images.add(image);
		return image;
	}

	@Override
	public void removeCurrentImage() {
		this.images.clear();
	}
}
