package com.saym.eventory.event.domain.repository;

import com.saym.eventory.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepository extends JpaRepository<Event, Long> {
}
