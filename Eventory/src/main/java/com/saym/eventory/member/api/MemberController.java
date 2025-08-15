package com.saym.eventory.member.api;

import com.saym.eventory.common.template.RspTemplate;
import com.saym.eventory.member.api.dto.request.ChangeUserTypeRequestDto;
import com.saym.eventory.member.api.dto.response.MemberResponseDto;
import com.saym.eventory.member.application.MemberService;
import com.saym.eventory.member.domain.UserType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "Member API", description = "사용자 관련 API 입니다. (사용자 인증, 사용자 정보)")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "사용자 유형 변경 요청 (사업자등록증 이미지 업로드 포함), GENERAL은 null 허용하여 바로 승인됩니다.")
    @PatchMapping(value = "/{memberId}/user-type", consumes = {"multipart/form-data"})
    public RspTemplate<MemberResponseDto> changeUserType(
            @PathVariable Long memberId,
            @RequestPart("userType") String userTypeStr,
            @RequestPart(value = "businessLicenseFile", required = false) MultipartFile file
    ) {
        ChangeUserTypeRequestDto dto = new ChangeUserTypeRequestDto(
                UserType.valueOf(userTypeStr),
                file
        );
        return RspTemplate.ok(memberService.changeUserType(memberId, dto));
    }

    @Operation(summary = "사업자 승인")
    @PostMapping("/{memberId}/approve")
    public RspTemplate<MemberResponseDto> approveBusiness(@PathVariable Long memberId) {
        return RspTemplate.ok(memberService.approveBusiness(memberId));
    }

    @Operation(summary = "사업자 승인 거부")
    @PostMapping("/{memberId}/reject")
    public RspTemplate<MemberResponseDto> rejectBusiness(@PathVariable Long memberId) {
        return RspTemplate.ok(memberService.rejectBusiness(memberId));
    }
}
