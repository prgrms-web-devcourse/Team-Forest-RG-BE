package com.prgrms.rg.domain.ridingpost.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import com.prgrms.rg.domain.common.file.model.AttachedImage;
import com.prgrms.rg.domain.common.file.model.ImageOwner;
import com.prgrms.rg.domain.common.file.model.TemporaryImage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RidingSubSection implements ImageOwner {

	private static final int MAX_IMAGE_LIST_SIZE = 5;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private RidingPost post;

	@Size(min = 1, max = 30)
	private String title;

	//1-500자
	@Size(min = 1, max = 500, message = "contents의 제약 조건에 위배됩니다.")
	@Column(name = "content")
	private String content;

	//0-5개의 사진. 사진 용량 체크 -> service 단에서 해야할듯 ..
	@OneToMany(mappedBy = "subInformation", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<SubImage> images = new ArrayList<>();

	public RidingSubSection(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void assignPost(RidingPost post) {
		this.post = post;
	}

	public List<String> getImageUrlAsList() {
		return getImages().stream()
			.map(SubImage::getUrl)
			.collect(Collectors.toList());
	}

	@Override
	public AttachedImage attach(TemporaryImage storedImage) {
		var image = new SubImage(storedImage, this);
		images.add(image);
		return image;
	}

	@Override
	public void removeCurrentImage() {
		this.images.clear();
	}
}
