package com.saym.eventory.ai.domain.repository;

import com.saym.eventory.ai.domain.Ai;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiRepository extends JpaRepository<Ai, Long> {
}
