package com.saym.eventory.bookmark.domain;


import com.saym.eventory.event.domain.Event;
import com.saym.eventory.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

    @Id
    @Column(name = "bookmark_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    public Bookmark(Member member, Event event) {
        this.member = member;
        this.event = event;
    }


}
