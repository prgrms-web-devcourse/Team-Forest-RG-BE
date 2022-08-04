package com.prgrms.rg.infrastructure.file;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.prgrms.rg.domain.common.file.application.FileIOException;
import com.prgrms.rg.domain.common.file.application.FileStore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class S3FileStore implements FileStore {

	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	public String bucketName;

	@Override
	public String save(MultipartFile multipartFile, String fileName) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(multipartFile.getSize());
		metadata.setContentType(multipartFile.getContentType());

		try (InputStream inputStream = multipartFile.getInputStream()) {
			amazonS3Client.putObject(
				new PutObjectRequest(bucketName, fileName, inputStream, metadata)
					.withCannedAcl(CannedAccessControlList.PublicRead));
			return amazonS3Client.getUrl(bucketName, fileName).toString();
		} catch (IOException e) {
			throw new FileIOException(e.getMessage(), e);
		}
	}

	@Override
	public void delete(String fileUrl) {
		amazonS3Client.deleteObject(bucketName, fileUrlToStoredFileName(fileUrl));
	}

	private String fileUrlToStoredFileName(String fileUrl) {
		return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
	}
}
