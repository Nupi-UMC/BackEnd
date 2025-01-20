package com.project.nupibe.domain.member.repository;

import com.project.nupibe.domain.member.entity.MemberStore;
import com.project.nupibe.domain.store.entity.Store;
import org.hibernate.query.results.complete.ModelPartReferenceEmbeddable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberStoreRepository extends JpaRepository<MemberStore, Long> {

    boolean existsByMemberIdAndStoreId(Long memberId, Long storeId);

    @Query("SELECT ms.store FROM MemberStore ms WHERE ms.member.id = :memberId")
    List<Store> findStoresByMemberId(@Param("memberId") Long memberId);
}

