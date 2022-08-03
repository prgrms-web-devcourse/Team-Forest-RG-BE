package com.prgrms.rg.domain.common.file.application;

import java.util.List;

import com.prgrms.rg.domain.common.file.model.AttachedImage;
import com.prgrms.rg.domain.common.file.model.ImageOwner;

public interface ImageAttachManger {
	<T extends ImageOwner> List<AttachedImage> store(List<Long> temporaryImageIds, T owner);

	<T extends ImageOwner> AttachedImage store(Long temporaryImageId, T owner);

	<T1 extends AttachedImage, T2 extends ImageOwner> void delete(List<T1> files, T2 attached);

	<T1 extends AttachedImage, T2 extends ImageOwner> void delete(T1 file, T2 attached);
}
