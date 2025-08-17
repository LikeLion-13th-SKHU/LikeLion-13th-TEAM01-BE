package com.saym.eventory.ai.domain.repository;

import com.saym.eventory.ai.domain.Ai;
import com.saym.eventory.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiRepository extends JpaRepository<Ai, Long> {
    List<Ai> findByMemberOrderByChatDateDesc(Member member);
}
