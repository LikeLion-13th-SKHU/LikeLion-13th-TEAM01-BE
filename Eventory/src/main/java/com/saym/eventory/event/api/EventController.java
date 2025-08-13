package com.saym.eventory.event.api;

import com.saym.eventory.common.template.RspTemplate;
import com.saym.eventory.event.api.dto.response.EventDetailResponseDto;
import com.saym.eventory.event.api.dto.response.EventInfoResponseDto;
import com.saym.eventory.event.application.EventService;
import com.saym.eventory.global.token.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {

    private final EventService eventService;
    private final TokenProvider tokenProvider;


    // 행사 불러오기 (제목, 날짜, 사진)
    @GetMapping
    @Operation(method = "GET", summary = "행사 목록 조회", description = "여러 개의 행사(제목, 날짜, 사진)를 조회합니다.")
    public ResponseEntity<RspTemplate<List<EventInfoResponseDto>>> getAllEvents() {
        List<EventInfoResponseDto> events = eventService.getEventList();

        RspTemplate<List<EventInfoResponseDto>> response = RspTemplate.success(
                HttpStatus.OK,
                "행사 목록 조회 성공",
                events
        );

        return ResponseEntity.ok(response);
    }

    // 행사 상세정보 조회 (내용, 주소)
    @GetMapping("/detail/{eventId}")
    @Operation(method = "GET", summary = "행사 상세정보 조회", description = "행사의 상세정보를 조회합니다.")
    public ResponseEntity<RspTemplate<EventDetailResponseDto>> getEventDetail(@PathVariable("eventId") Long eventId) {
        EventDetailResponseDto eventDetailResponseDto = eventService.getEventDetail(eventId);

        RspTemplate<EventDetailResponseDto> response = RspTemplate.success(
                HttpStatus.OK,
                "행사 상세정보 조회 성공",
                eventDetailResponseDto
        );

        return ResponseEntity.ok(response);
    }

    // 북마크에 행사 저장
    @PostMapping("/bookmark/{eventId}")
    @Operation(method = "POST", summary = "행사 북마크 저장")
    public ResponseEntity<RspTemplate<Void>> addBookmark(@PathVariable Long eventId, HttpServletRequest request) {
        // HTTP 요청 헤더에서 토큰을 추출
        String token = tokenProvider.resolveToken(request);

        // 토큰에서 memberId를 파싱
        String memberIdStr = tokenProvider.getAuthentication(token).getName();
        Long memberId = Long.parseLong(memberIdStr);

        // memberId를 사용해서 서비스 로직 실행
        eventService.addBookmark(memberId, eventId);

        RspTemplate<Void> response = RspTemplate.success(
                HttpStatus.OK,
                "북마크 추가 성공",
                null
        );
        return ResponseEntity.ok(response);
    }

    // 북마크 목록 조회
    @GetMapping("/bookmark")
    @Operation(method = "GET", summary = "북마크 목록 조회", description = "회원이 북마크한 행사 목록을 조회합니다.")
    public ResponseEntity<RspTemplate<List<EventInfoResponseDto>>> getBookmarks(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        String memberIdStr = tokenProvider.getAuthentication(token).getName();
        Long memberId = Long.parseLong(memberIdStr);

        List<EventInfoResponseDto> bookmarks = eventService.getBookmarks(memberId);

        RspTemplate<List<EventInfoResponseDto>> response = RspTemplate.success(
                HttpStatus.OK,
                "북마크 목록 조회 성공",
                bookmarks
        );
        return ResponseEntity.ok(response);
    }
}