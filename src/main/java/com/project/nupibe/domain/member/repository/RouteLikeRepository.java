package com.project.nupibe.domain.member.repository;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.entity.RouteLike;
import com.project.nupibe.domain.route.entity.Route;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteLikeRepository extends CrudRepository<RouteLike, Long> {
    @Query("SELECT mr.route FROM RouteLike mr WHERE mr.member = :member")
    List<Route> findByMember(Member member);

    boolean existsByMemberIdAndRouteId(Long memberId, Long routeId);

    @Query("SELECT ms FROM RouteLike ms WHERE ms.member = :member and ms.route = :route")
    RouteLike findByMemberandRoute(@Param("member") Member member, @Param("route") Route route);

}
