package com.saym.eventory.event.api;


import com.saym.eventory.common.template.RspTemplate;
import com.saym.eventory.event.api.dto.request.EventRequestDto;
import com.saym.eventory.event.api.dto.response.EventInfoResponseDto;
import com.saym.eventory.event.application.EventService;
import io.swagger.v3.oas.annotations.Operation;
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

    @PostMapping
    @Operation(summary = "행사 등록", description = "행사를 등록합니다.")
    public ResponseEntity<RspTemplate<Long>> createEvent(@RequestBody EventRequestDto eventRequestDto) {
        Long id = eventService.createEvent(eventRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RspTemplate.success(HttpStatus.CREATED, "행사 생성 성공", id));
    }

    @PutMapping("/{eventId}")
    @Operation(summary = "행사 수정", description = "행사 정보를 수정합니다.")
    public ResponseEntity<RspTemplate<Void>> updateEvent(@PathVariable Long eventId, @RequestBody EventRequestDto eventRequestDto) {
        eventService.updateEvent(eventId, eventRequestDto);
        return ResponseEntity.ok(RspTemplate.success(HttpStatus.OK, "행사 수정 성공", null));
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "행사 삭제", description = "행사를 삭제합니다.")
    public ResponseEntity<RspTemplate<Void>> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok(RspTemplate.success(HttpStatus.OK, "행사 삭제 성공", null));
    }
}