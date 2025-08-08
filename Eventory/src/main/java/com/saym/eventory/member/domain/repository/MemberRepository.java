package com.saym.eventory.member.domain.repository;


import com.saym.eventory.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);
    Optional<Member> findByKakaoId(Long id);
    Optional<Member>findByEmail(String email);
    Optional<Member> findByRefreshToken(String refreshToken);
}