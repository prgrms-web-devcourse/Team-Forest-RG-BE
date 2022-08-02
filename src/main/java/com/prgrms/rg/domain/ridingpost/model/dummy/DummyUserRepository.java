package com.prgrms.rg.domain.ridingpost.model.dummy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.user.model.User;

//User도메인에서 repo 구현시 삭제 예정
public interface DummyUserRepository extends JpaRepository<User, Long> {

}
