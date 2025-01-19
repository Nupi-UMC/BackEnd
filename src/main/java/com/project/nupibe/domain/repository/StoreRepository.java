package com.project.nupibe.domain.repository;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

}
