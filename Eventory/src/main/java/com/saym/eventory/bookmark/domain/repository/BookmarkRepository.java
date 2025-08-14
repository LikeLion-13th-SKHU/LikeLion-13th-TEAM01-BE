package com.saym.eventory.bookmark.domain.repository;

import com.saym.eventory.bookmark.domain.Bookmark;
import com.saym.eventory.event.domain.Event;
import com.saym.eventory.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByMemberAndEvent(Member member, Event event);
    List<Bookmark> findByMember(Member member);
}
