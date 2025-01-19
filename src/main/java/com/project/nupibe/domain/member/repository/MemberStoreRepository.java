package com.project.nupibe.domain.member.repository;

import com.project.nupibe.domain.member.entity.MemberStore;
import com.project.nupibe.domain.store.entity.Store;
import org.hibernate.query.results.complete.ModelPartReferenceEmbeddable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStoreRepository extends JpaRepository<MemberStore, Long> {
    boolean existsByMemberIdAndStoreId(Long memberId, Long storeId);

}

