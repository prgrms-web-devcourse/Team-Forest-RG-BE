package com.prgrms.rg.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.common.file.model.TemporaryImage;
import com.prgrms.rg.domain.common.file.model.TemporaryImageRepository;

public interface JpaTemporaryImageRepository extends JpaRepository<TemporaryImage, Long>, TemporaryImageRepository {
}
