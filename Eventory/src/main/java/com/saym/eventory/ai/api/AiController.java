package com.saym.eventory.ai.api;

import com.saym.eventory.ai.api.dto.request.AiRequestDto;
import com.saym.eventory.ai.api.dto.response.AiChatResponseDto;
import com.saym.eventory.ai.api.dto.response.AiResultResponseDto;
import com.saym.eventory.ai.application.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@Tag(name = "Ai API", description = "Ai 관련 API입니다.")
public class AiController {

    private final AiService aiService;

    @Operation(
            summary = "행사 아이디어를 입력하여 AI 평가 출력",
            description = "기획안 이미지, 행사명, 행사 설명을 AI에 전달하여 분석 결과(고려사항, 슬로건, 긍정/부정 비율)를 반환합니다. ai 채팅 연장은 유료 기능 (한 채팅당 하나의 결과물만 제공, 연장 X)"
    )
    @PostMapping("/analyze")
    public AiResultResponseDto analyzeIdea(@RequestBody AiRequestDto aiRequestDto, Principal principal) throws Exception {
        return aiService.analyzeIdea(aiRequestDto, principal);
    }

    @Operation(
            summary = "채팅 내용 조회",
            description = "AI 분석 기록을 기반으로 해당 채팅 정보를 조회합니다. <br>" +
                    "image_url: 기획안 이미지, chatContent: 사용자 입력 데이터, chatDate: 채팅한 날짜, considerations: 기획 시 고려할 요소, slogans: 홍보 문구 및 추천 슬로건, userEvaluation: 사용자 예상 평가 (positive_percentage: 긍정, negative_reasons: 부정)"
    )
    @GetMapping("/chat-record/{aiId}")
    public AiChatResponseDto getChatInfo(@PathVariable Long aiId) throws Exception {
        return aiService.getChatInfo(aiId);
    }
}
