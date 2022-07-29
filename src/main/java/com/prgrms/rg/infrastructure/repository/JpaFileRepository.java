package com.prgrms.rg.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.common.file.FileRepository;
import com.prgrms.rg.domain.common.file.StoredFile;

public interface JpaFileRepository extends JpaRepository<StoredFile, Long>, FileRepository {
}
