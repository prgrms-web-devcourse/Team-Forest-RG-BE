package com.prgrms.rg.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.common.file.model.ImageRepository;
import com.prgrms.rg.domain.common.file.model.StoredImage;

public interface JpaImageRepository extends JpaRepository<StoredImage, Long>, ImageRepository {
}
