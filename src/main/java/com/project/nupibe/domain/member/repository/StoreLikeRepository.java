package com.project.nupibe.domain.member.repository;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.entity.StoreLike;
import com.project.nupibe.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreLikeRepository extends JpaRepository<StoreLike, Long> {

    boolean existsByMemberIdAndStoreId(Long memberId, Long storeId);

    @Query("SELECT ms.store FROM StoreLike ms WHERE ms.member.id = :memberId")
    List<Store> findStoresByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT ms FROM StoreLike ms WHERE ms.member = :member and ms.store = :store")
    StoreLike findByMemberandStore(@Param("member") Member member, @Param("store") Store store);
}
