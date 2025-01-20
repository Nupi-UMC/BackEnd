package com.project.nupibe.domain.store.repository;

import com.project.nupibe.domain.member.entity.MemberStore;
import com.project.nupibe.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberStoreRepository extends JpaRepository<MemberStore, Long> {

    @Query("SELECT ms.store FROM MemberStore ms WHERE ms.member.id = :memberId")
    List<Store> findStoresByMemberId(@Param("memberId") Long memberId);
}

