package com.saym.eventory.ai.domain;

import com.saym.eventory.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Ai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // 행사명
    @Column(name = "title", nullable = false)
    private String title;

    // 사용자 입력
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String description;

    // 채팅한 날짜
    @Column(name = "chat_date")
    private LocalDateTime chatDate;

    // 기획안 이미지
    @Column(name = "image_url")
    private String image_url;

    // 기획 시 고려할 요소
    @Column(columnDefinition = "TEXT")
    private String considerationsJson;

    // 홍보 문구 및 추천 슬로건
    @Column(columnDefinition = "TEXT")
    private String slogansJson;

    // 사용자 예상 평가
    @Column(columnDefinition = "TEXT")
    private String userEvaluationJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
