package com.saym.eventory.ai.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saym.eventory.ai.api.dto.request.AiRequestDto;
import com.saym.eventory.ai.api.dto.response.AiChatResponseDto;
import com.saym.eventory.ai.api.dto.response.AiResultResponseDto;
import com.saym.eventory.ai.domain.Ai;
import com.saym.eventory.ai.domain.repository.AiRepository;
import com.saym.eventory.common.exception.CustomException;
import com.saym.eventory.common.exception.Error;
import com.saym.eventory.member.domain.Member;
import com.saym.eventory.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {

    private final AiRepository aiRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Value("${ai.base-url}")
    private String aiBaseUrl;

    @Value("${ai.chat-path}")
    private String aiChatPath;

    private final RestTemplate restTemplate = new RestTemplate();

    private Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.MEMBER_NOT_FOUND, "회원 정보를 찾을 수 없습니다."));
    }

    public AiResultResponseDto analyzeIdea(AiRequestDto aiRequestDto, Principal principal) throws Exception {
        Long memberId = Long.parseLong(principal.getName());
        Member member = getMemberById(memberId);

        String requestUrl = aiBaseUrl + aiChatPath;
        log.info("AI requestUrl = {} data = {}", requestUrl, aiRequestDto);

        AiResultResponseDto aiResult;
        try {
            aiResult = restTemplate.postForObject(
                    requestUrl,
                    aiRequestDto,
                    AiResultResponseDto.class
            );
        } catch (Exception e) {
            log.error("AI 서버 호출 중 오류 발생", e);
            throw new CustomException(Error.AI_SERVER_ERROR, "AI 서버 호출 실패");
        }

        if (aiResult == null) {
            throw new CustomException(Error.AI_SERVER_ERROR, "AI 서버에서 응답이 없습니다.");
        }

        log.info("AI 응답 수신: {}", aiResult);

        aiResult = new AiResultResponseDto(
                aiRequestDto.title(),
                aiResult.considerations(),
                aiResult.slogans(),
                aiResult.userEvaluation()
        );

        // DB 저장
        Ai ai = Ai.builder()
                .title(aiRequestDto.title())
                .description(aiRequestDto.description())
                .image_url(aiRequestDto.image_url())
                .considerationsJson(objectMapper.writeValueAsString(aiResult.considerations()))
                .slogansJson(objectMapper.writeValueAsString(aiResult.slogans()))
                .userEvaluationJson(objectMapper.writeValueAsString(aiResult.userEvaluation()))
                .chatDate(LocalDateTime.now())
                .member(member)
                .build();

        aiRepository.save(ai);
        return aiResult;
    }

    public AiChatResponseDto getChatInfo(Long aiId) throws Exception {
        Ai ai = aiRepository.findById(aiId)
                .orElseThrow(() -> new CustomException(Error.AI_NOT_FOUND, "AI 데이터를 찾을 수 없습니다. ID: " + aiId));

        List<String> considerations = objectMapper.readValue(ai.getConsiderationsJson(), List.class);
        List<String> slogans = objectMapper.readValue(ai.getSlogansJson(), List.class);
        AiResultResponseDto.UserEvaluation userEvaluation =
                objectMapper.readValue(ai.getUserEvaluationJson(), AiResultResponseDto.UserEvaluation.class);

        return new AiChatResponseDto(
                ai.getImage_url(),
                ai.getDescription(),
                ai.getChatDate(),
                considerations,
                slogans,
                userEvaluation
        );
    }
}