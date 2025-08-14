package com.saym.eventory.member.api;

import com.saym.eventory.common.template.RspTemplate;
import com.saym.eventory.member.api.dto.request.UpdateMemberInfoRequestDto;
import com.saym.eventory.member.api.dto.response.MemberInfoResponseDto;
import com.saym.eventory.member.application.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
@Tag(name = "Mypage API", description = "내 정보 조회 및 수정 API 입니다.")
public class MypageController {

    private final MemberService memberService;

    @Operation(summary = "사용자 정보 조회")
    @GetMapping
    public RspTemplate<MemberInfoResponseDto> getMypage(Principal principal) {
        MemberInfoResponseDto memberInfoResDto = memberService.getMemberInfo(principal);
        return RspTemplate.success(HttpStatus.OK, "내 정보 조회 성공", memberInfoResDto);
    }

    @Operation(summary = "사용자 정보 수정")
    @PutMapping
    public RspTemplate<MemberInfoResponseDto> updateName(Principal principal,
                                                    @Valid @RequestBody UpdateMemberInfoRequestDto requestDto) {
        MemberInfoResponseDto updatedInfo = memberService.updateName(principal, requestDto);
        return RspTemplate.success(HttpStatus.OK, "이름 변경 성공", updatedInfo);
    }
}