package com.saym.eventory.event.domain.repository;

import com.saym.eventory.event.domain.Event;
import com.saym.eventory.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
}
