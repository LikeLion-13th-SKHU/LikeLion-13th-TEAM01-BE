package com.saym.eventory.ai.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saym.eventory.ai.api.dto.request.AiRequestDto;
import com.saym.eventory.ai.api.dto.response.AiChatResponseDto;
import com.saym.eventory.ai.api.dto.response.AiResultResponseDto;
import com.saym.eventory.ai.domain.Ai;
import com.saym.eventory.ai.domain.repository.AiRepository;
import com.saym.eventory.common.exception.CustomException;
import com.saym.eventory.common.exception.Error;
import com.saym.eventory.global.s3.service.S3Service;
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
    private final S3Service s3Service;

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

        // S3 이미지 업로드
        // Todo: 이미지 업로드 유무 확인
        String imageUrl = null;
        if (aiRequestDto.imageFile() != null && !aiRequestDto.imageFile().isEmpty()) {
            try {
                imageUrl = s3Service.uploadFile(aiRequestDto.imageFile());
                log.info("AI 요청용 이미지 업로드 성공: {}", imageUrl);
            } catch (Exception e) {
                log.error("AI 요청용 이미지 업로드 실패", e);
                throw new CustomException(Error.FILE_UPLOAD_FAILED, "AI 이미지 업로드 실패");
            }
        }

        // AI 서버 호출 (텍스트 + 이미지 url)
        var requestBody = new java.util.HashMap<String, Object>();
        requestBody.put("title", aiRequestDto.title());
        requestBody.put("description", aiRequestDto.description());
        requestBody.put("image_url", imageUrl);

        String requestUrl = aiBaseUrl + aiChatPath;
        AiResultResponseDto aiResult;
        try {
            aiResult = restTemplate.postForObject(
                    requestUrl,
                    requestBody,
                    AiResultResponseDto.class
            );
        } catch (Exception e) {
            log.error("AI 서버 호출 중 오류 발생", e);
            throw new CustomException(Error.AI_SERVER_ERROR, "AI 서버 호출 실패");
        }

        if (aiResult == null) {
            throw new CustomException(Error.AI_SERVER_ERROR, "AI 서버에서 응답이 없습니다.");
        }

        // 디비 저장
        Ai ai = Ai.builder()
                .title(aiRequestDto.title())
                .description(aiRequestDto.description())
                .image_url(imageUrl) // S3 업로드 url (없으면 null)
                .considerationsJson(objectMapper.writeValueAsString(aiResult.considerations()))
                .slogansJson(objectMapper.writeValueAsString(aiResult.slogans()))
                .userEvaluationJson(objectMapper.writeValueAsString(aiResult.userEvaluation()))
                .chatDate(LocalDateTime.now())
                .member(member)
                .build();

        aiRepository.save(ai);

        AiResultResponseDto finalResult = new AiResultResponseDto(
                aiRequestDto.title(),
                aiResult.considerations(),
                aiResult.slogans(),
                aiResult.userEvaluation()
        );

        aiRepository.save(ai);

        return finalResult;
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