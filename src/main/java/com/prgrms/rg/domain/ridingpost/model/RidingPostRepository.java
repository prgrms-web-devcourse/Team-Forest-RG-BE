package com.prgrms.rg.domain.ridingpost.model;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.ridingpost.model.dummy.RidingPost;

public interface RidingPostRepository extends JpaRepository<RidingPost, Long> {
}
