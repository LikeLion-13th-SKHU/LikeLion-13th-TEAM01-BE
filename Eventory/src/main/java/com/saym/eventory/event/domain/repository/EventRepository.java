package com.saym.eventory.event.domain.repository;

import com.saym.eventory.event.domain.Area;
import com.saym.eventory.event.domain.Event;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long> {


    @Query("SELECT e FROM Event e " +
            "WHERE (:areas IS NULL OR e.area IN :areas) " +
            "AND (:startDate IS NULL OR e.eventStartDate >= :startDate) " +
            "AND (:endDate IS NULL OR e.eventEndDate <= :endDate)")
    List<Event> findByFilters(
            @Param("areas") List<Area> areas,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
