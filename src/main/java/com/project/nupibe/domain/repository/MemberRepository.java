package com.project.nupibe.domain.repository;

import com.project.nupibe.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository  extends JpaRepository<Member, Integer> {
    Optional<Member> findById(Long loginId);
}
