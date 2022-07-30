package com.prgrms.rg.domain.common.file;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileManager {
	<T extends ImageAttachable> List<StoredFile> store(List<MultipartFile> multipartFiles, T attached);

	<T extends ImageAttachable> StoredFile store(MultipartFile multipartFile, T attached);

	<T1 extends StoredFile, T2 extends ImageAttachable> void delete(List<T1> files, T2 attached);

	<T1 extends StoredFile, T2 extends ImageAttachable> void delete(T1 file, T2 attached);
}
