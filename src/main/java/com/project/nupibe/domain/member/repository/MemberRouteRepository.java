package com.project.nupibe.domain.member.repository;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.entity.MemberRoute;
import com.project.nupibe.domain.member.entity.MemberStore;
import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.store.entity.Store;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRouteRepository extends CrudRepository<MemberRoute, Long> {
    @Query("SELECT mr.route FROM MemberRoute mr WHERE mr.member = :member")
    List<Route> findByMember(Member member);

    boolean existsByMemberIdAndRouteId(Long memberId, Long routeId);

    @Query("SELECT ms FROM MemberRoute ms WHERE ms.member = :member and ms.route = :route")
    MemberRoute findByMemberandRoute(@Param("member") Member member, @Param("route") Route route);

}
