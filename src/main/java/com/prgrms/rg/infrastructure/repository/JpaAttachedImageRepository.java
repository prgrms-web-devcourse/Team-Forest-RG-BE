package com.prgrms.rg.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.common.file.model.AttachedImage;
import com.prgrms.rg.domain.common.file.model.AttachedImageRepository;

public interface JpaAttachedImageRepository extends JpaRepository<AttachedImage, Long>, AttachedImageRepository {
}
