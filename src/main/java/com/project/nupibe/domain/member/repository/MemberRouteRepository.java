package com.project.nupibe.domain.member.repository;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.entity.MemberRoute;
import com.project.nupibe.domain.route.entity.Route;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MemberRouteRepository extends CrudRepository<MemberRoute, Long> {
    @Query("SELECT mr.route FROM MemberRoute mr WHERE mr.member = :member")
    List<Route> findByMember(Member member);
}
