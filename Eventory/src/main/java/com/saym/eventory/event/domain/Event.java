package com.saym.eventory.event.domain;

import com.saym.eventory.bookmark.domain.Bookmark;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "event_start_date", nullable = false)
    private LocalDate eventStartDate;

    @Column(name = "event_end_date", nullable = false)
    private LocalDate eventEndDate;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "area", nullable = false)
    private Area area;

    @Column(name = "content", length = 300)
    private String content;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Builder
    private Event(String eventName, LocalDate eventStartDate, LocalDate eventEndDate, String pictureUrl, Area area, String content) {
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.pictureUrl = pictureUrl;
        this.area = area;
        this.content = content;
    }



}
